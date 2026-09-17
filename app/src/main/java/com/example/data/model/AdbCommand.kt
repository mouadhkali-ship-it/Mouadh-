package com.example.data.model

enum class CommandCategory(val titleAr: String, val iconName: String) {
    BASICS("الأساسيات والاتصال", "link"),
    APP_MANAGEMENT("إدارة التطبيقات", "apps"),
    FILE_TRANSFER("نقل الملفات والنسخ", "swap_horiz"),
    DEVICE_CONTROL("التحكم بالشاشة والنظام", "settings_system_daydream"),
    LOGS_DEBUGGING("السجلات وتصحيح الأخطاء", "bug_report"),
    ADVANCED_TWEAKS("الإعدادات والصلاحيات المتقدمة", "tune")
}

enum class DangerLevel(val labelAr: String, val colorHex: Long) {
    SAFE("آمن تماماً", 0xFF10B981),
    MODERATE("انتبه أثناء الاستخدام", 0xFFF59E0B),
    DANGEROUS("حذر: قد يؤثر على النظام", 0xFFEF4444)
}

data class AdbCommand(
    val id: String,
    val command: String,
    val syntax: String,
    val category: CommandCategory,
    val titleAr: String,
    val descriptionAr: String,
    val detailedExplanationAr: String,
    val example: String,
    val simulatedOutput: String,
    val dangerLevel: DangerLevel,
    val commonUseCasesAr: List<String>,
    val tags: List<String> = emptyList()
)
