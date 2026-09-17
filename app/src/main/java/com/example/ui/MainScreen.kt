package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.ui.components.CommandDetailDialog
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.CommandsScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.TerminalScreen
import com.example.ui.screens.TutorialsScreen
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.TerminalBg
import com.example.ui.theme.TerminalBorder
import com.example.ui.theme.TerminalCard
import com.example.viewmodel.AdbViewModel
import com.example.viewmodel.AppTab
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: AdbViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val filteredCommands by viewModel.filteredCommands.collectAsStateWithLifecycle()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsStateWithLifecycle()
    val selectedCommandForDetail by viewModel.selectedCommandForDetail.collectAsStateWithLifecycle()
    val selectedTutorialForDetail by viewModel.selectedTutorialForDetail.collectAsStateWithLifecycle()

    val terminalLogs by viewModel.terminalLogs.collectAsStateWithLifecycle()
    val terminalInput by viewModel.terminalInput.collectAsStateWithLifecycle()

    val quizState by viewModel.quizState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val showCopySnackbar = {
        coroutineScope.launch {
            snackbarHostState.showSnackbar("تم نسخ الأمر إلى الحافظة بنجاح")
        }
    }

    // Force RTL for natural Arabic reading experience
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag("main_screen_scaffold"),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(CyberEmerald),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_adb_logo),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = "تعلم ADB بالعربية",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Android Debug Bridge Manual & Terminal",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ElectricCyan,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = TerminalBg,
                        titleContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = TerminalCard,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = TerminalBorder,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .testTag("bottom_navigation_bar")
                ) {
                    // 1. Commands
                    NavigationBarItem(
                        selected = currentTab == AppTab.COMMANDS,
                        onClick = { viewModel.selectTab(AppTab.COMMANDS) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.COMMANDS) Icons.Filled.Terminal else Icons.Outlined.Terminal,
                                contentDescription = AppTab.COMMANDS.titleAr
                            )
                        },
                        label = { Text(AppTab.COMMANDS.titleAr, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF022C1A),
                            selectedTextColor = CyberEmerald,
                            indicatorColor = CyberEmerald,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        )
                    )

                    // 2. Terminal
                    NavigationBarItem(
                        selected = currentTab == AppTab.TERMINAL,
                        onClick = { viewModel.selectTab(AppTab.TERMINAL) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.TERMINAL) Icons.Filled.PlayCircle else Icons.Outlined.PlayCircleOutline,
                                contentDescription = AppTab.TERMINAL.titleAr
                            )
                        },
                        label = { Text(AppTab.TERMINAL.titleAr, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF003547),
                            selectedTextColor = ElectricCyan,
                            indicatorColor = ElectricCyan,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        )
                    )

                    // 3. Tutorials
                    NavigationBarItem(
                        selected = currentTab == AppTab.TUTORIALS,
                        onClick = { viewModel.selectTab(AppTab.TUTORIALS) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.TUTORIALS) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                                contentDescription = AppTab.TUTORIALS.titleAr
                            )
                        },
                        label = { Text(AppTab.TUTORIALS.titleAr, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF022C1A),
                            selectedTextColor = CyberEmerald,
                            indicatorColor = CyberEmerald,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        )
                    )

                    // 4. Quiz
                    NavigationBarItem(
                        selected = currentTab == AppTab.QUIZ,
                        onClick = { viewModel.selectTab(AppTab.QUIZ) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.QUIZ) Icons.Filled.EmojiEvents else Icons.Outlined.EmojiEvents,
                                contentDescription = AppTab.QUIZ.titleAr
                            )
                        },
                        label = { Text(AppTab.QUIZ.titleAr, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF451A03),
                            selectedTextColor = AmberWarning,
                            indicatorColor = AmberWarning,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        )
                    )

                    // 5. Bookmarks
                    NavigationBarItem(
                        selected = currentTab == AppTab.BOOKMARKS,
                        onClick = { viewModel.selectTab(AppTab.BOOKMARKS) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (bookmarkedIds.isNotEmpty()) {
                                        Badge(containerColor = AmberWarning) {
                                            Text(
                                                text = bookmarkedIds.size.toString(),
                                                color = Color(0xFF451A03),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentTab == AppTab.BOOKMARKS) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = AppTab.BOOKMARKS.titleAr
                                )
                            }
                        },
                        label = { Text(AppTab.BOOKMARKS.titleAr, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF451A03),
                            selectedTextColor = AmberWarning,
                            indicatorColor = AmberWarning,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        )
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = TerminalBg
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    AppTab.COMMANDS -> {
                        CommandsScreen(
                            commands = filteredCommands,
                            bookmarkedIds = bookmarkedIds,
                            searchQuery = searchQuery,
                            selectedCategory = selectedCategory,
                            onSearchChange = viewModel::updateSearchQuery,
                            onCategorySelect = viewModel::selectCategory,
                            onCommandClick = viewModel::showCommandDetail,
                            onBookmarkToggle = viewModel::toggleBookmark,
                            onRunInTerminal = viewModel::runInTerminalAndSwitchTab,
                            onCopied = { showCopySnackbar() }
                        )
                    }

                    AppTab.TERMINAL -> {
                        TerminalScreen(
                            logs = terminalLogs,
                            inputText = terminalInput,
                            onInputChange = viewModel::setTerminalInput,
                            onExecute = { override -> viewModel.executeTerminalCommand(override) },
                            onClear = viewModel::clearTerminal
                        )
                    }

                    AppTab.TUTORIALS -> {
                        TutorialsScreen(
                            tutorials = viewModel.allTutorials,
                            selectedTutorial = selectedTutorialForDetail,
                            onSelectTutorial = viewModel::showTutorialDetail,
                            onBackToList = viewModel::closeTutorialDetail,
                            onCopied = { showCopySnackbar() }
                        )
                    }

                    AppTab.QUIZ -> {
                        QuizScreen(
                            quizState = quizState,
                            questions = viewModel.quizQuestions,
                            onSelectOption = viewModel::selectQuizOption,
                            onSubmitAnswer = viewModel::submitQuizAnswer,
                            onNextQuestion = viewModel::nextQuizQuestion,
                            onRestartQuiz = viewModel::restartQuiz
                        )
                    }

                    AppTab.BOOKMARKS -> {
                        val bookmarkedCommands = viewModel.allCommands.filter {
                            bookmarkedIds.contains(it.id)
                        }
                        BookmarksScreen(
                            bookmarkedCommands = bookmarkedCommands,
                            onCommandClick = viewModel::showCommandDetail,
                            onBookmarkToggle = viewModel::toggleBookmark,
                            onRunInTerminal = viewModel::runInTerminalAndSwitchTab,
                            onCopied = { showCopySnackbar() }
                        )
                    }
                }

                // Command Details Dialog
                selectedCommandForDetail?.let { cmd ->
                    CommandDetailDialog(
                        command = cmd,
                        isBookmarked = bookmarkedIds.contains(cmd.id),
                        onBookmarkToggle = { viewModel.toggleBookmark(cmd.id) },
                        onDismiss = viewModel::closeCommandDetail,
                        onRunInTerminal = { viewModel.runInTerminalAndSwitchTab(cmd.command) },
                        onCopied = { showCopySnackbar() }
                    )
                }
            }
        }
    }
}
