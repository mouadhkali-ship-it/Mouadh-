package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AdbDataSource
import com.example.data.local.AppDatabase
import com.example.data.model.AdbCommand
import com.example.data.model.AdbQuizQuestion
import com.example.data.model.AdbTutorial
import com.example.data.model.CommandCategory
import com.example.data.repository.AdbRepository
import com.example.data.repository.TerminalExecutionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab(val titleAr: String) {
    COMMANDS("الأوامر"),
    TERMINAL("المحاكي"),
    TUTORIALS("الشروحات"),
    QUIZ("الاختبار"),
    BOOKMARKS("المفضلة")
}

data class TerminalLogItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val command: String,
    val output: String,
    val isSuccess: Boolean,
    val explanationAr: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class QuizUiState(
    val currentQuestionIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val correctAnswersCount: Int = 0,
    val isFinished: Boolean = false
)

class AdbViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AdbRepository

    init {
        val database = AppDatabase.getInstance(application)
        repository = AdbRepository(database.adbBookmarkDao())
    }

    private val _currentTab = MutableStateFlow(AppTab.COMMANDS)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<CommandCategory?>(null)
    val selectedCategory: StateFlow<CommandCategory?> = _selectedCategory.asStateFlow()

    private val _selectedCommandForDetail = MutableStateFlow<AdbCommand?>(null)
    val selectedCommandForDetail: StateFlow<AdbCommand?> = _selectedCommandForDetail.asStateFlow()

    private val _selectedTutorialForDetail = MutableStateFlow<AdbTutorial?>(null)
    val selectedTutorialForDetail: StateFlow<AdbTutorial?> = _selectedTutorialForDetail.asStateFlow()

    val bookmarkedIds: StateFlow<List<String>> = repository.getBookmarkedCommandIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCommands: List<AdbCommand> = repository.getAllCommands()
    val allTutorials: List<AdbTutorial> = repository.getTutorials()
    val quizQuestions: List<AdbQuizQuestion> = repository.getQuizQuestions()

    val filteredCommands: StateFlow<List<AdbCommand>> = combine(
        _searchQuery,
        _selectedCategory
    ) { query, category ->
        val trimmedQuery = query.trim().lowercase()
        allCommands.filter { cmd ->
            val matchesCategory = category == null || cmd.category == category
            val matchesQuery = trimmedQuery.isEmpty() ||
                cmd.command.lowercase().contains(trimmedQuery) ||
                cmd.titleAr.contains(trimmedQuery) ||
                cmd.descriptionAr.contains(trimmedQuery) ||
                cmd.tags.any { it.lowercase().contains(trimmedQuery) }
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allCommands)

    // Terminal State
    private val _terminalInput = MutableStateFlow("adb devices")
    val terminalInput: StateFlow<String> = _terminalInput.asStateFlow()

    private val _terminalLogs = MutableStateFlow<List<TerminalLogItem>>(
        listOf(
            TerminalLogItem(
                command = "adb version",
                output = "Android Debug Bridge version 1.0.41\nVersion 35.0.2-12146317",
                isSuccess = true,
                explanationAr = "مرحباً بك في محاكي ADB باللغة العربية! اكتب أو اختر أي أمر لتجربته ومعرفة كيفية عمله."
            ),
            TerminalLogItem(
                command = "adb devices",
                output = """List of devices attached
emulator-5554          device product:sdk_gphone64_arm64 model:Pixel_8
192.168.1.45:5555      device product:husky model:Pixel_8_Pro""",
                isSuccess = true,
                explanationAr = "تم فحص الأجهزة المتصلة: يوجد جهازان جاهزان لتلقي الأوامر."
            )
        )
    )
    val terminalLogs: StateFlow<List<TerminalLogItem>> = _terminalLogs.asStateFlow()

    // Quiz State
    private val _quizState = MutableStateFlow(QuizUiState())
    val quizState: StateFlow<QuizUiState> = _quizState.asStateFlow()

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: CommandCategory?) {
        _selectedCategory.value = category
    }

    fun showCommandDetail(command: AdbCommand) {
        _selectedCommandForDetail.value = command
    }

    fun closeCommandDetail() {
        _selectedCommandForDetail.value = null
    }

    fun showTutorialDetail(tutorial: AdbTutorial) {
        _selectedTutorialForDetail.value = tutorial
    }

    fun closeTutorialDetail() {
        _selectedTutorialForDetail.value = null
    }

    fun toggleBookmark(commandId: String) {
        viewModelScope.launch {
            repository.toggleBookmark(commandId)
        }
    }

    fun setTerminalInput(input: String) {
        _terminalInput.value = input
    }

    fun executeTerminalCommand(inputOverride: String? = null) {
        val commandToRun = (inputOverride ?: _terminalInput.value).trim()
        if (commandToRun.isEmpty()) return

        val result = repository.executeSimulatedCommand(commandToRun)
        if (result.output == "CLEAR_SCREEN") {
            _terminalLogs.value = emptyList()
        } else {
            _terminalLogs.update { list ->
                list + TerminalLogItem(
                    command = commandToRun,
                    output = result.output,
                    isSuccess = result.isSuccess,
                    explanationAr = result.explanationAr
                )
            }
        }
        _terminalInput.value = ""
    }

    fun clearTerminal() {
        _terminalLogs.value = emptyList()
    }

    fun runInTerminalAndSwitchTab(commandText: String) {
        _terminalInput.value = commandText
        executeTerminalCommand(commandText)
        _currentTab.value = AppTab.TERMINAL
        _selectedCommandForDetail.value = null
    }

    // Quiz functions
    fun selectQuizOption(index: Int) {
        if (_quizState.value.isAnswerSubmitted) return
        _quizState.update { it.copy(selectedOptionIndex = index) }
    }

    fun submitQuizAnswer() {
        val state = _quizState.value
        val selectedIdx = state.selectedOptionIndex ?: return
        if (state.isAnswerSubmitted) return

        val currentQ = quizQuestions.getOrNull(state.currentQuestionIndex) ?: return
        val isCorrect = selectedIdx == currentQ.correctOptionIndex

        _quizState.update {
            it.copy(
                isAnswerSubmitted = true,
                correctAnswersCount = if (isCorrect) it.correctAnswersCount + 1 else it.correctAnswersCount
            )
        }
    }

    fun nextQuizQuestion() {
        val state = _quizState.value
        val nextIdx = state.currentQuestionIndex + 1
        if (nextIdx < quizQuestions.size) {
            _quizState.update {
                it.copy(
                    currentQuestionIndex = nextIdx,
                    selectedOptionIndex = null,
                    isAnswerSubmitted = false
                )
            }
        } else {
            _quizState.update { it.copy(isFinished = true) }
        }
    }

    fun restartQuiz() {
        _quizState.value = QuizUiState()
    }
}
