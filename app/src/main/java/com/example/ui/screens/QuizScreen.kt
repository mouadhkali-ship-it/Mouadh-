package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdbQuizQuestion
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.TerminalBorder
import com.example.ui.theme.TerminalCard
import com.example.ui.theme.TerminalSurface
import com.example.viewmodel.QuizUiState

@Composable
fun QuizScreen(
    quizState: QuizUiState,
    questions: List<AdbQuizQuestion>,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onRestartQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (quizState.isFinished) {
        QuizFinishedView(
            totalQuestions = questions.size,
            correctCount = quizState.correctAnswersCount,
            onRestart = onRestartQuiz,
            modifier = modifier
        )
    } else {
        val currentQuestion = questions.getOrNull(quizState.currentQuestionIndex)
        if (currentQuestion != null) {
            QuizQuestionView(
                questionNumber = quizState.currentQuestionIndex + 1,
                totalQuestions = questions.size,
                question = currentQuestion,
                selectedOptionIndex = quizState.selectedOptionIndex,
                isSubmitted = quizState.isAnswerSubmitted,
                onSelectOption = onSelectOption,
                onSubmit = onSubmitAnswer,
                onNext = onNextQuestion,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun QuizQuestionView(
    questionNumber: Int,
    totalQuestions: Int,
    question: AdbQuizQuestion,
    selectedOptionIndex: Int?,
    isSubmitted: Boolean,
    onSelectOption: (Int) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = questionNumber.toFloat() / totalQuestions.toFloat()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp)
            .testTag("quiz_screen")
    ) {
        // Top Progress Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = TerminalCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, TerminalBorder),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "السؤال $questionNumber من $totalQuestions",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyberEmerald,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = CyberEmerald,
                    trackColor = TerminalSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = TerminalCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, TerminalBorder),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(top = 2.dp)
                    )
                    Text(
                        text = question.questionAr,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 26.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Options
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            question.optionsAr.forEachIndexed { index, optionText ->
                val isSelected = selectedOptionIndex == index
                val isCorrectAnswer = index == question.correctOptionIndex

                val borderColor = when {
                    isSubmitted && isCorrectAnswer -> CyberEmerald
                    isSubmitted && isSelected && !isCorrectAnswer -> Color(0xFFEF4444)
                    isSelected -> ElectricCyan
                    else -> TerminalBorder
                }

                val bgColor = when {
                    isSubmitted && isCorrectAnswer -> CyberEmerald.copy(alpha = 0.15f)
                    isSubmitted && isSelected && !isCorrectAnswer -> Color(0xFFEF4444).copy(alpha = 0.15f)
                    isSelected -> ElectricCyan.copy(alpha = 0.12f)
                    else -> TerminalCard
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                        .clickable(enabled = !isSubmitted) { onSelectOption(index) }
                        .testTag("quiz_option_$index"),
                    color = bgColor
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Radio circle / status icon
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSubmitted && isCorrectAnswer -> CyberEmerald
                                        isSubmitted && isSelected && !isCorrectAnswer -> Color(0xFFEF4444)
                                        isSelected -> ElectricCyan
                                        else -> TerminalSurface
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSubmitted && isCorrectAnswer) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF022C1A),
                                    modifier = Modifier.size(16.dp)
                                )
                            } else if (isSubmitted && isSelected && !isCorrectAnswer) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected || (isSubmitted && isCorrectAnswer)) Color.White else Color(0xFFCBD5E1),
                            fontWeight = if (isSelected || (isSubmitted && isCorrectAnswer)) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Explanation if submitted
        AnimatedVisibility(visible = isSubmitted) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = TerminalSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TerminalBorder),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = AmberWarning,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "توضيح الإجابة الصحيحة:",
                                style = MaterialTheme.typography.labelMedium,
                                color = AmberWarning,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = question.explanationAr,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFE2E8F0),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Button (Submit or Next)
        if (!isSubmitted) {
            Button(
                onClick = onSubmit,
                enabled = selectedOptionIndex != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("quiz_submit_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyberEmerald,
                    contentColor = Color(0xFF022C1A),
                    disabledContainerColor = TerminalSurface,
                    disabledContentColor = Color(0xFF64748B)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "تأكيد الإجابة",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        } else {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("quiz_next_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricCyan,
                    contentColor = Color(0xFF042F2E)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (questionNumber == totalQuestions) "عرض النتيجة النهائية" else "السؤال التالي",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun QuizFinishedView(
    totalQuestions: Int,
    correctCount: Int,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val percentage = ((correctCount.toFloat() / totalQuestions.toFloat()) * 100).toInt()

    val (rankTitle, rankDesc, rankColor) = when {
        percentage >= 90 -> Triple(
            "خبير أنظمة أندرويد و ADB 🏆",
            "أداء مبهر ومثالي! لديك إلمام كامل بجميع أوامر ومكونات ADB.",
            CyberEmerald
        )
        percentage >= 70 -> Triple(
            "مستخدم متقدم في ADB 🚀",
            "أداء ممتاز جداً! يمكنك التعامل مع معظم سيناريوهات التثبيت والتصحيح.",
            ElectricCyan
        )
        percentage >= 50 -> Triple(
            "مستوى جيد 💡",
            "لديك فهم أساسي جيد، ننصحك بمراجعة الشروحات وقسم الأوامر لتعزيز معلوماتك.",
            AmberWarning
        )
        else -> Triple(
            "مبتدئ يحتاج للمزيد من التدريب 📚",
            "لا تقلق! ابدأ بقراءة شروحات التطبيق وجرب الأوامر في المحاكي التفاعلي ثم أعد الاختبار.",
            Color(0xFFEF4444)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("quiz_finished_view"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, TerminalBorder, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = TerminalCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(rankColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = rankColor,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "نتيجة الاختبار",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF94A3B8)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$correctCount / $totalQuestions",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "نسبة النجاح: $percentage%",
                    style = MaterialTheme.typography.labelLarge,
                    color = rankColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, rankColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                    color = TerminalSurface
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = rankTitle,
                            style = MaterialTheme.typography.titleSmall,
                            color = rankColor,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = rankDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFCBD5E1),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("quiz_restart_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberEmerald,
                        contentColor = Color(0xFF022C1A)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "إعادة الاختبار من البداية",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
