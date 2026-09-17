package com.example.data.repository

import com.example.data.AdbDataSource
import com.example.data.local.AdbBookmarkDao
import com.example.data.local.AdbBookmarkEntity
import com.example.data.model.AdbCommand
import com.example.data.model.AdbQuizQuestion
import com.example.data.model.AdbTutorial
import com.example.data.model.CommandCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class TerminalExecutionResult(
    val commandExecuted: String,
    val output: String,
    val isSuccess: Boolean,
    val explanationAr: String? = null
)

class AdbRepository(private val bookmarkDao: AdbBookmarkDao) {

    fun getAllCommands(): List<AdbCommand> = AdbDataSource.commands

    fun getTutorials(): List<AdbTutorial> = AdbDataSource.tutorials

    fun getQuizQuestions(): List<AdbQuizQuestion> = AdbDataSource.quizQuestions

    fun getBookmarkedCommandIds(): Flow<List<String>> = bookmarkDao.getAllBookmarkedIds()

    suspend fun toggleBookmark(commandId: String) {
        if (bookmarkDao.isBookmarked(commandId)) {
            bookmarkDao.deleteBookmark(commandId)
        } else {
            bookmarkDao.insertBookmark(AdbBookmarkEntity(commandId = commandId))
        }
    }

    suspend fun isBookmarked(commandId: String): Boolean {
        return bookmarkDao.isBookmarked(commandId)
    }

    fun executeSimulatedCommand(rawInput: String): TerminalExecutionResult {
        val trimmed = rawInput.trim()
        if (trimmed.isEmpty()) {
            return TerminalExecutionResult(
                commandExecuted = "",
                output = "",
                isSuccess = true
            )
        }

        if (trimmed == "clear" || trimmed == "cls") {
            return TerminalExecutionResult(
                commandExecuted = trimmed,
                output = "CLEAR_SCREEN",
                isSuccess = true
            )
        }

        // Direct matching against known catalog
        val matchedCommand = AdbDataSource.commands.find { cmd ->
            trimmed.equals(cmd.command, ignoreCase = true) ||
            trimmed.startsWith("${cmd.command} ", ignoreCase = true) ||
            trimmed.equals(cmd.example, ignoreCase = true)
        }

        if (matchedCommand != null) {
            return TerminalExecutionResult(
                commandExecuted = trimmed,
                output = matchedCommand.simulatedOutput,
                isSuccess = true,
                explanationAr = "${matchedCommand.titleAr}: ${matchedCommand.descriptionAr}"
            )
        }

        // Dynamic parsing for standard variations
        val lower = trimmed.lowercase()
        return when {
            lower == "adb version" -> {
                TerminalExecutionResult(
                    commandExecuted = trimmed,
                    output = "Android Debug Bridge version 1.0.41\nVersion 35.0.2-12146317\nInstalled as /platform-tools/adb",
                    isSuccess = true,
                    explanationAr = "عرض رقم إصدار حزمة أدوات منصة أندرويد (Platform Tools) المثبتة."
                )
            }
            lower == "adb help" || lower == "adb --help" -> {
                TerminalExecutionResult(
                    commandExecuted = trimmed,
                    output = """Android Debug Bridge version 1.0.41
Usage: adb [options] <command>
Global options:
  -d         use USB device
  -e         use TCP/IP device (emulator)
  -s SERIAL  use device with given serial
General commands:
  devices [-l]             list connected devices
  help                     show this help message
  version                  show version num
Networking:
  connect HOST[:PORT]      connect to device via TCP/IP
  disconnect [HOST[:PORT]] disconnect from given TCP/IP device
App management:
  install [-r] [-d] APK    push and install package
  uninstall [-k] PACKAGE   remove package
Shell:
  shell                    run remote interactive shell
  shell <command>          run remote shell command""",
                    isSuccess = true,
                    explanationAr = "شاشة مساعدة ADB العامة التي توضح الخيارات الأساسية وقائمة الأوامر."
                )
            }
            lower.startsWith("adb shell pm list packages") -> {
                TerminalExecutionResult(
                    commandExecuted = trimmed,
                    output = """package:com.android.systemui
package:com.google.android.apps.photos
package:com.google.android.youtube
package:com.whatsapp
package:com.spotify.music
package:org.telegram.messenger""",
                    isSuccess = true,
                    explanationAr = "سرد حزم التطبيقات المثبتة على جهاز أندرويد."
                )
            }
            lower.startsWith("adb shell getprop") -> {
                TerminalExecutionResult(
                    commandExecuted = trimmed,
                    output = """[ro.build.version.release]: [15]
[ro.build.version.sdk]: [35]
[ro.product.brand]: [google]
[ro.product.manufacturer]: [Google]
[ro.product.model]: [Pixel 9 Pro]
[ro.product.cpu.abi]: [arm64-v8a]""",
                    isSuccess = true,
                    explanationAr = "قراءة خصائص ومعلومات النظام الأساسية (Build Properties)."
                )
            }
            lower.startsWith("adb shell dumpsys") -> {
                TerminalExecutionResult(
                    commandExecuted = trimmed,
                    output = """DUMP OF SERVICE battery:
  AC powered: false
  USB powered: true
  status: 2 (Charging)
  health: 2 (Good)
  present: true
  level: 94
  voltage: 4230
  temperature: 284 (28.4°C)""",
                    isSuccess = true,
                    explanationAr = "استخراج تقرير تفصيلي عن حالة خدمة النظام المطلوبة."
                )
            }
            lower.startsWith("adb shell input") -> {
                TerminalExecutionResult(
                    commandExecuted = trimmed,
                    output = "Event dispatched successfully to input subsystem.",
                    isSuccess = true,
                    explanationAr = "تم إرسال حدث الإدخال التفاعلي إلى النظام بنجاح."
                )
            }
            lower.startsWith("adb shell") -> {
                TerminalExecutionResult(
                    commandExecuted = trimmed,
                    output = "Linux localhost 6.1.75-android15 #1 SMP PREEMPT\nroot@android:/ # exit",
                    isSuccess = true,
                    explanationAr = "تنفيذ أمر سطر أوامر داخل نظام لينكس الخاص بأندرويد."
                )
            }
            lower.startsWith("adb reboot") -> {
                TerminalExecutionResult(
                    commandExecuted = trimmed,
                    output = "Rebooting device...",
                    isSuccess = true,
                    explanationAr = "تم إرسال إشارة إعادة التشغيل للجهاز."
                )
            }
            lower.startsWith("adb ") -> {
                TerminalExecutionResult(
                    commandExecuted = trimmed,
                    output = "adb: command executed (simulated response: OK)\nSuccess",
                    isSuccess = true,
                    explanationAr = "تم تنفيذ أمر ADB بنجاح في المحاكي."
                )
            }
            else -> {
                TerminalExecutionResult(
                    commandExecuted = trimmed,
                    output = "bash: $trimmed: command not found\n\nتلميح: جميع أوامر ADB تبدأ بالبادئة 'adb'، مثلاً جرب كتابة:\n  adb devices\n  adb shell dumpsys battery\n  adb help",
                    isSuccess = false,
                    explanationAr = "أمر غير معروف؛ تأكد من كتابة 'adb' متبوعاً بالأمر المطلوب."
                )
            }
        }
    }
}
