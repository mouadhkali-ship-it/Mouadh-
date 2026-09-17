package com.example.data.model

data class TutorialStep(
    val stepNumber: Int,
    val titleAr: String,
    val descriptionAr: String,
    val commandSnippet: String? = null,
    val tipAr: String? = null
)

data class AdbTutorial(
    val id: String,
    val titleAr: String,
    val subtitleAr: String,
    val readTimeMinutes: Int,
    val osTarget: String, // Windows / Mac / Linux / Android
    val iconName: String,
    val overviewAr: String,
    val steps: List<TutorialStep>,
    val importantNotesAr: List<String> = emptyList()
)

data class AdbQuizQuestion(
    val id: Int,
    val questionAr: String,
    val optionsAr: List<String>,
    val correctOptionIndex: Int,
    val explanationAr: String
)
