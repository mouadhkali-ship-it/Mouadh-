package com.example.data

import com.example.data.model.AdbCommand
import com.example.data.model.AdbQuizQuestion
import com.example.data.model.AdbTutorial
import com.example.data.model.CommandCategory
import com.example.data.model.DangerLevel
import com.example.data.model.TutorialStep

object AdbDataSource {

    val commands: List<AdbCommand> = listOf(
        // 1. الأساسيات والاتصال
        AdbCommand(
            id = "adb_devices",
            command = "adb devices",
            syntax = "adb devices [-l]",
            category = CommandCategory.BASICS,
            titleAr = "فحص الأجهزة المتصلة",
            descriptionAr = "يعرض قائمة بجميع أجهزة أندرويد المتصلة بالكمبيوتر وحالة كل جهاز.",
            detailedExplanationAr = "الأمر الأساسي الذي يبدأ به كل مطور أو مستخدم. يقوم بالاتصال بخادم ADB المحلي وسرد أرقام السيريال للأجهزة المتصلة عبر USB أو الشبكة مع حالتها (device تعني جاهز، unauthorized تعني يحتاج الموافقة من شاشة الهاتف، offline تعني غير متصل بشكل سليم). استخدام خيار -l يعطي تفاصيل إضافية مثل موديل الجهاز ورقم الطراز.",
            example = "adb devices -l",
            simulatedOutput = """List of devices attached
emulator-5554          device product:sdk_gphone64_arm64 model:Pixel_8 device:emu64a
192.168.1.45:5555      device product:husky model:Pixel_8_Pro device:husky""",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "التحقق من نجاح توصيل الهاتف بالكمبيوتر",
                "معرفة الرقم التسلسلي (Serial Number) للجهاز",
                "التأكد من قبول إذن تصحيح أخطاء USB على الهاتف"
            ),
            tags = listOf("devices", "connection", "usb", "serial", "check")
        ),
        AdbCommand(
            id = "adb_tcpip",
            command = "adb tcpip 5555",
            syntax = "adb tcpip <port>",
            category = CommandCategory.BASICS,
            titleAr = "تفعيل وضع الاتصال اللاسلكي (TCP/IP)",
            descriptionAr = "إعادة تشغيل خدمة ADB على الجهاز لتقبل الاتصالات عبر شبكة الواي فاي على منفذ معين.",
            detailedExplanationAr = "يسمح هذا الأمر بفصل كابل الـ USB ومواصلة العمل عبر شبكة الـ Wi-Fi. بعد تشغيل هذا الأمر بينما الجهاز متصل بالكابل، يمكنك بعد ذلك استخدام أمر adb connect مع عنوان IP الخاص بهاتفك للاتصال لاسلكياً.",
            example = "adb tcpip 5555",
            simulatedOutput = "restarting in TCP mode port: 5555",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "الاستغناء عن كابل USB أثناء البرمجة والتجربة",
                "الاتصال بالهاتف عن بُعد على نفس شبكة الواي فاي المحلية",
                "فحص التطبيقات في وضع حر بدون قيود الكابل"
            ),
            tags = listOf("wifi", "wireless", "tcpip", "port", "network")
        ),
        AdbCommand(
            id = "adb_connect",
            command = "adb connect",
            syntax = "adb connect <ip-address>[:<port>]",
            category = CommandCategory.BASICS,
            titleAr = "الاتصال بالجهاز لاسلكياً عبر IP",
            descriptionAr = "يربط الكمبيوتر بهاتف أندرويد عبر عنوان IP للشبكة المحلية.",
            detailedExplanationAr = "بعد تفعيل منفذ TCP/IP، يمكنك الاتصال بالجهاز مباشرة عبر عنوان IP المحلي لهاتفك (تستخرجه من إعدادات الهاتف > حول الهاتف > الحالة > عنوان IP). المنفذ الافتراضي هو 5555.",
            example = "adb connect 192.168.1.105:5555",
            simulatedOutput = "connected to 192.168.1.105:5555",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "الاتصال بالهاتف أو الشاشة الذكية (Android TV) بدون كابل",
                "تطوير وتجربة التطبيقات في نفس الشبكة المحلية"
            ),
            tags = listOf("connect", "wifi", "ip", "wireless")
        ),
        AdbCommand(
            id = "adb_disconnect",
            command = "adb disconnect",
            syntax = "adb disconnect [<ip-address>[:<port>]]",
            category = CommandCategory.BASICS,
            titleAr = "قطع الاتصال اللاسلكي",
            descriptionAr = "يفصل الارتباط بجهاز أو بجميع الأجهزة المتصلة عبر شبكة Wi-Fi.",
            detailedExplanationAr = "يستخدم لإنهاء الجلسة اللاسلكية وتنظيف الاتصالات المفتوحة لمنع استهلاك البطارية أو عند التبديل لشبكة أخرى.",
            example = "adb disconnect 192.168.1.105:5555",
            simulatedOutput = "disconnected 192.168.1.105:5555",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "قطع الاتصال بجهاز معين بعد الانتهاء من العمل",
                "إلغاء جميع اتصالات الشبكة النشطة دفعة واحدة"
            ),
            tags = listOf("disconnect", "wifi", "close")
        ),
        AdbCommand(
            id = "adb_kill_server",
            command = "adb kill-server",
            syntax = "adb kill-server",
            category = CommandCategory.BASICS,
            titleAr = "إيقاف خادم ADB في الكمبيوتر",
            descriptionAr = "يوقف عملية خادم ADB الخلفية لحل مشاكل التعليق والتجميد.",
            detailedExplanationAr = "يعمل ADB بنظام العميل والخادم على الكمبيوتر. أحياناً يعلق الخادم أو يحدث تعارض في المنافذ (بسبب برامج المحاكيات أو برامج أخرى). أمر kill-server يغلق الخدمة تماماً، وبعدها أي أمر ADB سيعيد تشغيلها بشكل نظيف.",
            example = "adb kill-server",
            simulatedOutput = "* server killed successfully *",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "حل مشكلة عدم تعرف الكمبيوتر على الهاتف فجأة",
                "إعادة تشغيل الخادم بعد تحديث التعريفات",
                "تفريغ ذاكرة ADB العالقة"
            ),
            tags = listOf("restart", "kill", "server", "reset", "troubleshoot")
        ),
        AdbCommand(
            id = "adb_start_server",
            command = "adb start-server",
            syntax = "adb start-server",
            category = CommandCategory.BASICS,
            titleAr = "تشغيل خادم ADB يدوياً",
            descriptionAr = "يبدأ تشغيل الخادم المحلي على المنفذ الافتراضي 5037.",
            detailedExplanationAr = "يقوم بفحص هل الخادم يعمل أم لا، وإذا كان متوقفاً ينشئ العملية في الخلفية للبدء في مراقبة منافذ USB والشبكة.",
            example = "adb start-server",
            simulatedOutput = """* daemon not running; starting now at tcp:5037
* daemon started successfully""",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "بدء تشغيل الخدمة بعد إيقافها يدوياً بأمر kill-server"
            ),
            tags = listOf("start", "server", "daemon")
        ),

        // 2. إدارة التطبيقات
        AdbCommand(
            id = "adb_install",
            command = "adb install",
            syntax = "adb install [-r] [-d] [-g] <path_to_apk>",
            category = CommandCategory.APP_MANAGEMENT,
            titleAr = "تثبيت تطبيق APK على الهاتف",
            descriptionAr = "يثبت ملف حزمة أندرويد (APK) من الكمبيوتر مباشرة إلى الهاتف.",
            detailedExplanationAr = "يقوم برفع ملف الـ APK وتثبيته عبر مدير الحزم PackageManager. خيارات شائعة مفيدة: \n-r: إعادة التثبيت مع الاحتفاظ ببيانات التطبيق الحالي (Reinstall).\n-d: السماح بالرجوع لإصدار أقدم (Downgrade).\n-g: منح جميع الأذونات المطلوبة للتطبيق تلقائياً عند التثبيت (Grant all permissions).",
            example = "adb install -r myapp.apk",
            simulatedOutput = """Performing Streamed Install
Success""",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "تثبيت التطبيقات أثناء التطوير البرمجي بسرعة فائقة",
                "تثبيت ملفات APK وتحديثها دون الحاجة لنقلها لكرت الذاكرة أولاً",
                "منح الصلاحيات مباشرة دون ظهور مربعات حوار الأذونات (-g)"
            ),
            tags = listOf("install", "apk", "app", "package")
        ),
        AdbCommand(
            id = "adb_uninstall",
            command = "adb uninstall",
            syntax = "adb uninstall [-k] <package_name>",
            category = CommandCategory.APP_MANAGEMENT,
            titleAr = "إلغاء تثبيت تطبيق",
            descriptionAr = "يحذف التطبيق المحدد باسم الحزمة من جهاز أندرويد.",
            detailedExplanationAr = "يحذف التطبيق باستخدام اسم الحزمة (Package Name) مثل com.example.myapp. استخدام المفتاح -k يبقي على بيانات التطبيق وملفات الكاش ويحذف فقط كود التطبيق التنفيذي.",
            example = "adb uninstall com.whatsapp",
            simulatedOutput = "Success",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "إزالة التطبيقات بسهولة وسرعة",
                "تنظيف بيئة الاختبار وإعادة التثبيت النظيف"
            ),
            tags = listOf("uninstall", "remove", "delete", "package")
        ),
        AdbCommand(
            id = "adb_pm_list_packages",
            command = "adb shell pm list packages",
            syntax = "adb shell pm list packages [-3] [-s] [-d] [-e] [filter]",
            category = CommandCategory.APP_MANAGEMENT,
            titleAr = "عرض أسماء الحزم المثبتة",
            descriptionAr = "يسرد جميع أسماء الحزم (Packages) الموجودة على الجهاز مع إمكانية الفلترة.",
            detailedExplanationAr = "يقوم باستعلام PackageManager في أندرويد. خيارات الفلترة الهامة جداً:\n-3: يعرض فقط تطبيقات الطرف الثالث (التي ثبتها المستخدم وليست تابعة للنظام).\n-s: يعرض فقط تطبيقات النظام المدمجة (System apps).\n-d: يعرض التطبيقات المعطلة (Disabled).\n-e: يعرض التطبيقات المفعلة فقط (Enabled).\nيمكنك كتابة كلمة في النهاية للبحث، مثل: adb shell pm list packages google",
            example = "adb shell pm list packages -3",
            simulatedOutput = """package:com.spotify.music
package:com.whatsapp
package:org.telegram.messenger
package:com.instagram.android""",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "معرفة الاسم البرمجي لحزمة أي تطبيق ترغب في حذفه أو تعطيله",
                "فحص التطبيقات المثبتة وكشف البرمجيات غير المرغوبة"
            ),
            tags = listOf("packages", "list", "pm", "apps", "search")
        ),
        AdbCommand(
            id = "adb_pm_path",
            command = "adb shell pm path",
            syntax = "adb shell pm path <package_name>",
            category = CommandCategory.APP_MANAGEMENT,
            titleAr = "استخراج مسار ملف APK للتطبيق",
            descriptionAr = "يكشف المسار الدقيق لملف الـ APK المخزن في ذاكرة الهاتف لأي تطبيق.",
            detailedExplanationAr = "يحدد موقع ملف التطبيق الفعلي داخل مجلدات النظام مثل /data/app/. يمكنك استخدام هذا المسار مع أمر adb pull لسحب وحفظ ملف APK الأصلي لأي تطبيق مثبت على هاتفك إلى حاسوبك!",
            example = "adb shell pm path com.whatsapp",
            simulatedOutput = "package:/data/app/~~aX12b3c==/com.whatsapp-w399.../base.apk",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "استخراج ملف الـ APK لأي تطبيق مثبت على هاتفك بدون روت",
                "النسخ الاحتياطي لملفات البرامج قبل حذفها"
            ),
            tags = listOf("path", "apk", "extract", "pm")
        ),
        AdbCommand(
            id = "adb_pm_clear",
            command = "adb shell pm clear",
            syntax = "adb shell pm clear <package_name>",
            category = CommandCategory.APP_MANAGEMENT,
            titleAr = "مسح جميع بيانات وكاش التطبيق",
            descriptionAr = "يعيد ضبط التطبيق لحالته الأولى كأنه مثبت للتو، ماسحاً جميع البيانات المؤقتة والحسابات.",
            detailedExplanationAr = "يمسح مجلدات /data/data/<package_name> وقواعد البيانات والملفات المحفوظة ومفاتيح الدخول. عملية فورية توفر الوقت بدلاً من الدخول لإعدادات التطبيق يدوياً.",
            example = "adb shell pm clear com.example.app",
            simulatedOutput = "Success",
            dangerLevel = DangerLevel.MODERATE,
            commonUseCasesAr = listOf(
                "إعادة ضبط التطبيقات المتعطلة فوراً",
                "تفريغ مساحة التخزين الخاصة بالتطبيقات الضخمة",
                "بدء جلسة اختبار جديدة كلياً للمطورين"
            ),
            tags = listOf("clear", "cache", "data", "reset")
        ),
        AdbCommand(
            id = "adb_pm_disable_user",
            command = "adb shell pm disable-user --user 0",
            syntax = "adb shell pm disable-user --user 0 <package_name>",
            category = CommandCategory.APP_MANAGEMENT,
            titleAr = "تعطيل تطبيقات النظام (Debloat بدون روت)",
            descriptionAr = "يعطل تطبيق النظام المدمج غير القابل للحذف من الواجهة للمستخدم الحالي.",
            detailedExplanationAr = "أشهر أمر يستخدمه محبو التخصيص لحذف برامج النظام المنتفخة والمزعجة (Bloatware) التي تضعها الشركات المصنعة دون الحاجة إلى صلاحيات الروت. التطبيق يتوقف تماماً عن العمل ويختفي من قائمة التطبيقات ولا يستهلك الرام أو البطارية.",
            example = "adb shell pm disable-user --user 0 com.samsung.android.bixby.agent",
            simulatedOutput = "Package com.samsung.android.bixby.agent new state: disabled-user",
            dangerLevel = DangerLevel.MODERATE,
            commonUseCasesAr = listOf(
                "إيقاف تطبيقات النظام المزعجة وغير المفيدة (Debloat)",
                "تحسين استهلاك البطارية وتسريع الهاتف",
                "منع تطبيقات معينة من العمل في الخلفية إطلاقاً"
            ),
            tags = listOf("debloat", "disable", "bloatware", "battery", "optimize")
        ),
        AdbCommand(
            id = "adb_pm_enable",
            command = "adb shell pm enable",
            syntax = "adb shell pm enable <package_name>",
            category = CommandCategory.APP_MANAGEMENT,
            titleAr = "إعادة تفعيل تطبيق معطل",
            descriptionAr = "يعيد تنشيط أي تطبيق تم تعطيله سابقاً بأمر pm disable-user.",
            detailedExplanationAr = "إذا قمت بتعطيل تطبيق نظام واكتشفت أنك تحتاجه أو سبب اختفاء ميزة معينة، فهذا الأمر يعيده للعمل فوراً وبكل سهولة دون فقدان أي إعدادات.",
            example = "adb shell pm enable com.samsung.android.bixby.agent",
            simulatedOutput = "Package com.samsung.android.bixby.agent new state: enabled",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "استعادة التطبيقات المعطلة بأمان",
                "التراجع عن قرارات الـ Debloat الخاطئة"
            ),
            tags = listOf("enable", "restore", "activate")
        ),

        // 3. نقل الملفات والنسخ
        AdbCommand(
            id = "adb_push",
            command = "adb push",
            syntax = "adb push <local_path> <remote_path>",
            category = CommandCategory.FILE_TRANSFER,
            titleAr = "إرسال ملف من الكمبيوتر للهاتف",
            descriptionAr = "ينسخ ملف أو مجلد كامل من الحاسوب إلى مسار محدد في ذاكرة الهاتف.",
            detailedExplanationAr = "أسرع وأدق طريقة لنقل الملفات التنموية أو التحديثات أو الصور إلى الهاتف. يدعم نقل المجلدات بكامل محتوياتها بسرعة فائقة عبر بروتوكول ADB المباشر.",
            example = "adb push update.zip /sdcard/Download/",
            simulatedOutput = "update.zip: 1 file pushed, 0 skipped. 45.8 MB/s (145239845 bytes in 3.168s)",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "نقل ملفات النظام وحزم التحديثات والـ ROMs",
                "إرسال ملفات وسائط ومستندات مباشرة إلى مجلد التنزيلات"
            ),
            tags = listOf("push", "send", "copy", "transfer", "upload")
        ),
        AdbCommand(
            id = "adb_pull",
            command = "adb pull",
            syntax = "adb pull <remote_path> [<local_path>]",
            category = CommandCategory.FILE_TRANSFER,
            titleAr = "سحب ملف من الهاتف للكمبيوتر",
            descriptionAr = "ينسخ ملف أو مجلد من الهاتف ويحفظه على الكمبيوتر.",
            detailedExplanationAr = "يسمح باستخراج أي ملف من ذاكرة الهاتف إلى مجلد العمل الحالي على الحاسوب، بما في ذلك ملفات الصور وقواعد البيانات ومسودات السجلات.",
            example = "adb pull /sdcard/DCIM/Camera/photo.jpg ./",
            simulatedOutput = "/sdcard/DCIM/Camera/photo.jpg: 1 file pulled, 0 skipped. 32.1 MB/s (4829100 bytes in 0.150s)",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "استخراج الصور والملفات عند تعطل شاشة اللمس في الهاتف",
                "أخذ نسخ احتياطية للمستندات والملفات الشخصية الهامة",
                "سحب ملفات الـ APK المستخرجة بأمر pm path"
            ),
            tags = listOf("pull", "download", "backup", "save")
        ),
        AdbCommand(
            id = "adb_backup",
            command = "adb backup",
            syntax = "adb backup -apk -shared -all -f backup.ab",
            category = CommandCategory.FILE_TRANSFER,
            titleAr = "إنشاء نسخة احتياطية كاملة (Full Backup)",
            descriptionAr = "ينشئ أرشيفاً احتياطياً كاملاً لبيانات التطبيقات والملفات المشتركة بصيغة .ab.",
            detailedExplanationAr = "يطلب هذا الأمر تأكيداً وكلمة مرور على شاشة الهاتف، ثم يقوم بضغط وحفظ التطبيقات وبياناتها إلى ملف backup.ab على الكمبيوتر.",
            example = "adb backup -all -f my_phone_backup.ab",
            simulatedOutput = "Now unlock your device and confirm the backup operation...",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "حفظ نسخة أمان كاملة قبل ترقية نظام التشغيل أو تغيير الروم",
                "نقل محتويات الهاتف إلى حاسوب آمن"
            ),
            tags = listOf("backup", "archive", "data", "protect")
        ),
        AdbCommand(
            id = "adb_restore",
            command = "adb restore",
            syntax = "adb restore <path_to_backup.ab>",
            category = CommandCategory.FILE_TRANSFER,
            titleAr = "استعادة النسخة الاحتياطية",
            descriptionAr = "يستعيد بيانات الهاتف من ملف النسخة الاحتياطية الذي تم إنشاؤه سابقاً.",
            detailedExplanationAr = "يرسل ملف الأرشيف إلى الهاتف ويطلب تأكيد كلمة المرور لفك الضغط واسترجاع التطبيقات والبيانات.",
            example = "adb restore my_phone_backup.ab",
            simulatedOutput = "Now unlock your device and confirm the restore operation...",
            dangerLevel = DangerLevel.MODERATE,
            commonUseCasesAr = listOf(
                "استرجاع جميع البيانات بعد عمل فورمات للجهاز"
            ),
            tags = listOf("restore", "recovery", "data")
        ),

        // 4. التحكم بالشاشة والنظام
        AdbCommand(
            id = "adb_reboot",
            command = "adb reboot",
            syntax = "adb reboot [bootloader | recovery | edl | fastboot]",
            category = CommandCategory.DEVICE_CONTROL,
            titleAr = "إعادة تشغيل الهاتف أو الدخول للأوضاع الخاصة",
            descriptionAr = "يعيد تشغيل الجهاز فوراً إلى النظام العادي أو إلى وضع الريكفري أو البوت لودر.",
            detailedExplanationAr = "أمر أساسي ومهم جداً خاصة عند تعطل أزرار الصوت أو زر الطاقة في الهاتف:\n- adb reboot: إعادة تشغيل عادية للنظام.\n- adb reboot recovery: الدخول المباشر لوضع الاسترداد (Recovery Mode).\n- adb reboot bootloader: الدخول لوضع التحميل وتفليش الفاست بوت (Fastboot/Download Mode).\n- adb reboot edl: الدخول لوضع الطوارئ في معالجات كوالكوم (EDL Mode).",
            example = "adb reboot recovery",
            simulatedOutput = "Rebooting into recovery mode...",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "الدخول لوضع Recovery بدون الضغط على أزرار الصوت والطاقة",
                "الدخول لوضع Bootloader لتفليش التحديثات أو فك البوت لودر",
                "إعادة تشغيل الهاتف عن بُعد"
            ),
            tags = listOf("reboot", "restart", "recovery", "bootloader", "fastboot")
        ),
        AdbCommand(
            id = "adb_screencap",
            command = "adb shell screencap",
            syntax = "adb shell screencap -p <output_path.png>",
            category = CommandCategory.DEVICE_CONTROL,
            titleAr = "التقاط لقطة شاشة وحفظها (Screenshot)",
            descriptionAr = "يأخذ لقطة شاشة رقمية فورية للهاتف بدقة العرض الأصلية.",
            detailedExplanationAr = "مفيد جداً للمطورين لأخذ لقطات شاشة نقية وعالية الجودة وتوثيق الواجهات مباشرة في ملف PNG.",
            example = "adb shell screencap -p /sdcard/screen.png && adb pull /sdcard/screen.png .",
            simulatedOutput = "/sdcard/screen.png: 1 file pulled. 24.5 MB/s (1820491 bytes in 0.071s)",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "أخذ لقطات شاشة بسرعة فائقة للمستندات والتقارير",
                "التقاط الشاشة عند تلف أزرار الجهاز أو تعطل اللمس"
            ),
            tags = listOf("screencap", "screenshot", "screen", "capture", "photo")
        ),
        AdbCommand(
            id = "adb_screenrecord",
            command = "adb shell screenrecord",
            syntax = "adb shell screenrecord [--size WxH] [--bit-rate RATE] [--time-limit SEC] <path.mp4>",
            category = CommandCategory.DEVICE_CONTROL,
            titleAr = "تسجيل شاشة الهاتف كفيديو MP4",
            descriptionAr = "يبدأ تسجيل فيديو حي لكل ما يعرض على شاشة الهاتف مع التحكم بالدقة ومعدل البت.",
            detailedExplanationAr = "يسجل فيديو بجودة عالية مباشرة. الحد الأقصى الافتراضي هو 3 دقائق (180 ثانية) ويمكن إيقافه في أي لحظة بالضغط على Ctrl+C في الطرفية.",
            example = "adb shell screenrecord --size 1080x1920 --bit-rate 6000000 /sdcard/demo.mp4",
            simulatedOutput = "Recording started... press Ctrl+C to stop.",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "تسجيل عروض تقديمية وتجربة المستخدم لتطبيقاتك",
                "توثيق المشاكل والأخطاء البرمجية (Bugs) في التطبيقات"
            ),
            tags = listOf("screenrecord", "video", "record", "screen", "mp4")
        ),
        AdbCommand(
            id = "adb_wm_size",
            command = "adb shell wm size",
            syntax = "adb shell wm size [<width>x<height> | reset]",
            category = CommandCategory.DEVICE_CONTROL,
            titleAr = "تعديل دقة الشاشة (Resolution)",
            descriptionAr = "يغير الدقة المعروضة للشاشة برمجياً لاختبار تصاميم مختلفة أو لتوفير الطاقة.",
            detailedExplanationAr = "يتحكم بمدير النوافذ (WindowManager). يمكنك تغيير دقة العرض مثل جعلها 1080x2400 أو 720x1600. للرجوع للدقة الأصلية الافتراضية للجهاز استخدم: adb shell wm size reset.",
            example = "adb shell wm size 1080x1920",
            simulatedOutput = """Physical size: 1440x3120
Override size: 1080x1920""",
            dangerLevel = DangerLevel.MODERATE,
            commonUseCasesAr = listOf(
                "اختبار تجاوب تصميم التطبيق مع أحجام شاشات مختلفة",
                "تخفيض الدقة لزيادة معدل الإطارات (FPS) في الألعاب"
            ),
            tags = listOf("wm", "size", "resolution", "display", "screen")
        ),
        AdbCommand(
            id = "adb_wm_density",
            command = "adb shell wm density",
            syntax = "adb shell wm density [<dpi_value> | reset]",
            category = CommandCategory.DEVICE_CONTROL,
            titleAr = "تعديل كثافة الشاشة وحجم العناصر (DPI)",
            descriptionAr = "يتحكم في كثافة البكسلات (DPI)، مما يكبر أو يصغر حجم الأيقونات والخطوط على الشاشة.",
            detailedExplanationAr = "من أشهر أوامر التخصيص. تقليل قيمة الـ DPI يجعل الشاشة تتسع لعناصر أكثر (مثل واجهة التابلت)، وزيادتها تكبر الخطوط والأيقونات. للعودة للوضع الطبيعي: adb shell wm density reset.",
            example = "adb shell wm density 420",
            simulatedOutput = """Physical density: 560
Override density: 420""",
            dangerLevel = DangerLevel.MODERATE,
            commonUseCasesAr = listOf(
                "تصغير العناصر لتوسيع مساحة العمل والشاشة",
                "إصلاح مشاكل الحجم بعد تعديل الشاشة أو تغييرها"
            ),
            tags = listOf("wm", "density", "dpi", "scale", "icons")
        ),
        AdbCommand(
            id = "adb_input_tap",
            command = "adb shell input tap",
            syntax = "adb shell input tap <x> <y>",
            category = CommandCategory.DEVICE_CONTROL,
            titleAr = "محاكاة لمس الشاشة بإحداثيات محددة",
            descriptionAr = "ينفذ ضغطة لمس في نقطة معينة (X, Y) على شاشة الهاتف برمجياً.",
            detailedExplanationAr = "أداة رائعة لأتمتة الاختبارات (Automation). يرسل أمر لمس للنظام كأن إصبع المستخدم ضغط على تلك الإحداثيات بالضبط.",
            example = "adb shell input tap 500 1200",
            simulatedOutput = "Sent event: tap at (500, 1200)",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "أتمتة فتح التطبيقات والضغط على الأزرار",
                "تشغيل سكريبتات الفحص والتجربة التلقائية",
                "التحكم بالهاتف في حال انكسار جزء من زجاج اللمس"
            ),
            tags = listOf("input", "tap", "touch", "automate")
        ),
        AdbCommand(
            id = "adb_input_text",
            command = "adb shell input text",
            syntax = "adb shell input text <string>",
            category = CommandCategory.DEVICE_CONTROL,
            titleAr = "كتابة نص في حقل الإدخال على الهاتف",
            descriptionAr = "يرسل نصاً أو كلمة مرور من لوحة مفاتيح الكمبيوتر إلى حقل الإدخال النشط في الهاتف.",
            detailedExplanationAr = "يوفر عناء كتابة كلمات المرور الطويلة والمعقدة أو الروابط على كيبورد الهاتف الصغيرة، بمجرد وضع المؤشر في الحقل وتنفيذ الأمر سيتم كتابة النص فوراً.",
            example = "adb shell input text \"Hello_World_2026\"",
            simulatedOutput = "Sent text input event.",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "إدخال كلمات مرور معقدة وحسابات برمجية بسرعة",
                "تعبئة النماذج في التطبيقات أثناء الاختبار"
            ),
            tags = listOf("input", "text", "type", "keyboard")
        ),
        AdbCommand(
            id = "adb_input_keyevent",
            command = "adb shell input keyevent",
            syntax = "adb shell input keyevent <key_code>",
            category = CommandCategory.DEVICE_CONTROL,
            titleAr = "محاكاة أزرار الهاتف الفيزيائية (الرئيسية، الطاقة، الصوت)",
            descriptionAr = "يرسل ضغطة زر حقيقية مثل زر الهوم، الرجوع، الطاقة، قفل الشاشة أو رفع الصوت.",
            detailedExplanationAr = "أشهر رموز الأزرار (Keycodes):\n- 26: زر الطاقة (Power Button)\n- 3: الشاشة الرئيسية (Home)\n- 4: زر الرجوع (Back)\n- 24: رفع الصوت (Volume Up)\n- 25: خفض الصوت (Volume Down)\n- 82: زر القائمة (Menu)\n- 64: فتح المتصفح\n- 224: إيقاظ الشاشة (Wakeup)",
            example = "adb shell input keyevent 26",
            simulatedOutput = "Sent keyevent: 26 (KEYCODE_POWER)",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "تشغيل أو قفل الشاشة عند تعطل زر الباور الفيزيائي",
                "أتمتة الرجوع للخلف أو فتح القوائم في السكريبتات"
            ),
            tags = listOf("keyevent", "power", "home", "back", "buttons", "volume")
        ),

        // 5. السجلات وتصحيح الأخطاء
        AdbCommand(
            id = "adb_logcat",
            command = "adb logcat",
            syntax = "adb logcat [-c] [-d] [-s <tag>] [*:E | *:W | *:D | *:I]",
            category = CommandCategory.LOGS_DEBUGGING,
            titleAr = "قراءة سجل أحداث النظام والتطبيقات (Logcat)",
            descriptionAr = "يعرض تيار السجلات البرمجية المباشرة من النظام والخدمات والتطبيقات لكشف الأخطاء وسبب الانهيار (Crash).",
            detailedExplanationAr = "أهم أداة للمطورين لمعرفة سبب توقف التطبيقات. خيارات مفيدة جداً:\n- adb logcat -c: مسح وتفريغ السجلات السابقة للبدء بصفحة نظيفة.\n- adb logcat *:E: عرض رسائل الخطأ فقط (Errors) وتجاهل الباقي.\n- adb logcat -s \"MyApp\": تصفية السجل ليعرض فقط الرسائل الموسومة باسم تطبيقك.\n- adb logcat -d > crash.txt: حفظ السجل في ملف نصي على الحاسوب.",
            example = "adb logcat -d *:E",
            simulatedOutput = """--------- beginning of crash
09-17 11:20:15.102 12054 12054 E AndroidRuntime: FATAL EXCEPTION: main
09-17 11:20:15.102 12054 12054 E AndroidRuntime: Process: com.test.app, PID: 12054
09-17 11:20:15.103 12054 12054 E AndroidRuntime: java.lang.NullPointerException: Attempt to invoke virtual method on a null object reference""",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "معرفة سبب تعطل وتوقف التطبيق فجأة (Crash Diagnosis)",
                "متابعة رسائل الفحص (Debug Logs) في الوقت الفعلي",
                "حفظ تقرير بالأخطاء لمشاركته مع فريق البرمجة"
            ),
            tags = listOf("logcat", "logs", "crash", "debug", "error")
        ),
        AdbCommand(
            id = "adb_dumpsys_battery",
            command = "adb shell dumpsys battery",
            syntax = "adb shell dumpsys battery [set level <0-100> | reset]",
            category = CommandCategory.LOGS_DEBUGGING,
            titleAr = "معلومات البطارية ومحاكاة نسب الشحن",
            descriptionAr = "يكشف الحالة الفنية الدقيقة للبطارية، ويسمح بمحاكاة نسبة شحن محددة للاختبار.",
            detailedExplanationAr = "يعرض: نسبة الشحن، درجة الحرارة (بالمئوية ضرب 10)، فرق الجهد، الحالة الصحية (Good). كما يتيح للمطورين محاكاة انخفاض البطارية لاختبار كيفية تصرف التطبيق:\nadb shell dumpsys battery set level 5\nولإعادة البطارية لحالتها الحقيقية:\nadb shell dumpsys battery reset",
            example = "adb shell dumpsys battery",
            simulatedOutput = """Current Battery Service state:
  AC powered: false
  USB powered: true
  status: 2 (Charging)
  health: 2 (Good)
  present: true
  level: 86
  scale: 100
  voltage: 4185
  temperature: 295 (29.5°C)
  technology: Li-poly""",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "معرفة حرارة وصحة البطارية الفعلية بدقة",
                "اختبار وضع توفير الطاقة في التطبيقات بمحاكاة بطارية منخفضة"
            ),
            tags = listOf("dumpsys", "battery", "charge", "health", "temperature")
        ),
        AdbCommand(
            id = "adb_bugreport",
            command = "adb bugreport",
            syntax = "adb bugreport [<path_to_zip>]",
            category = CommandCategory.LOGS_DEBUGGING,
            titleAr = "توليد تقرير شامل عن حالة النظام (Bugreport)",
            descriptionAr = "يجمع أرشيفاً كاملاً وشاملاً لسجلات الهاتف، الذاكرة، استهلاك البطارية، والعمليات في ملف zip.",
            detailedExplanationAr = "التقرير الأقوى والأشمل في أندرويد لتقديم تقارير العيوب للمطورين ومحللي الأنظمة. يتم تحليله بأدوات مثل Battery Historian لكشف التطبيقات المستنزفة للبطارية.",
            example = "adb bugreport ./bugreport.zip",
            simulatedOutput = """Generating bugreport...
[100%] Dumped complete bugreport to ./bugreport.zip""",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "تحليل استنزاف البطارية العميق (Battery Drain Analysis)",
                "إرسال تقرير فني شامل للدعم البرمجي"
            ),
            tags = listOf("bugreport", "report", "diagnostic", "battery_historian")
        ),
        AdbCommand(
            id = "adb_shell_top",
            command = "adb shell top",
            syntax = "adb shell top [-m <count>] [-n <iterations>]",
            category = CommandCategory.LOGS_DEBUGGING,
            titleAr = "مراقبة استهلاك المعالج والرام في الوقت الفعلي",
            descriptionAr = "يعرض مدير مهام تفاعلي حي يوضح التطبيقات والعمليات الأكثر استهلاكاً لـ CPU و RAM.",
            detailedExplanationAr = "مشابه لأمر top الشهير في أنظمة Linux، يظهر معدل استخدام المعالج، الذاكرة، معرف العملية (PID)، واسم التطبيق.",
            example = "adb shell top -m 5",
            simulatedOutput = """User 12%, System 8%, IOW 0%, IRQ 0%
PID PR CPU% S  #THR     VSS     RSS PCY UID      NAME
1240  0  14% S   132 145028K  42104K  fg u0_a89   com.example.game
 982  1   4% S    68  89020K  21480K  fg system   system_server""",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "كشف التطبيقات المسببة لبطء وسخونة الهاتف",
                "مراقبة أداء التطبيق قيد البرمجة في الوقت الحقيقي"
            ),
            tags = listOf("top", "cpu", "ram", "performance", "task_manager")
        ),

        // 6. الصلاحيات والإعدادات المتقدمة
        AdbCommand(
            id = "adb_pm_grant",
            command = "adb shell pm grant",
            syntax = "adb shell pm grant <package_name> <permission_name>",
            category = CommandCategory.ADVANCED_TWEAKS,
            titleAr = "منح صلاحيات النظام الخاصة للتطبيقات (Write Secure Settings)",
            descriptionAr = "يمنح تطبيقاً معيناً أذونات نظام أندرويد الحساسة التي لا يمكن تفعيلها من واجهة الإعدادات العادية.",
            detailedExplanationAr = "أشهر أمر تستخدمه تطبيقات الأدوات مثل Tasker و Greenify و Shizuku و SystemUI Tuner. يمنح أذونات مثل WRITE_SECURE_SETTINGS و DUMP التي تسمح للتطبيق بالتحكم في الإعدادات العميقة للنظام دون الحاجة إلى روت.",
            example = "adb shell pm grant com.joaomgcd.tasker android.permission.WRITE_SECURE_SETTINGS",
            simulatedOutput = "Permission android.permission.WRITE_SECURE_SETTINGS granted successfully.",
            dangerLevel = DangerLevel.MODERATE,
            commonUseCasesAr = listOf(
                "تفعيل ميزات تطبيق Tasker و Macrodroid لأتمتة النظام",
                "منح صلاحيات تطبيقات توفير الطاقة وتعديل شريط الحالة",
                "تشغيل تطبيقات التخصيص المتقدمة بدون روت"
            ),
            tags = listOf("grant", "permission", "secure_settings", "tasker", "shizuku")
        ),
        AdbCommand(
            id = "adb_settings_put_global",
            command = "adb shell settings put",
            syntax = "adb shell settings put [global | system | secure] <key> <value>",
            category = CommandCategory.ADVANCED_TWEAKS,
            titleAr = "تعديل قيم إعدادات النظام الداخلية مباشرة",
            descriptionAr = "يغير قيم مفاتيح الإعدادات العالمية والأمنية في نظام أندرويد دون الدخول للقوائم.",
            detailedExplanationAr = "يتحكم بقاعدة بيانات SettingsProvider في أندرويد. يمكنك استخدامه لتسريع أو إلغاء تأثيرات الحركة (Animations) لجعل الهاتف يشعر بالسرعة الفائقة:\nadb shell settings put global window_animation_scale 0.5\nadb shell settings put global transition_animation_scale 0.5\nadb shell settings put global animator_duration_scale 0.5",
            example = "adb shell settings put global window_animation_scale 0.5",
            simulatedOutput = "Setting 'window_animation_scale' updated to '0.5'",
            dangerLevel = DangerLevel.MODERATE,
            commonUseCasesAr = listOf(
                "تسريع الهاتف بمضاعفة سرعة أنيميشن القوائم",
                "تغيير سلوك وضع توفير الطاقة وشبكات الاتصال برمجياً"
            ),
            tags = listOf("settings", "animation", "speed", "tweak", "global")
        ),
        AdbCommand(
            id = "adb_getprop",
            command = "adb shell getprop",
            syntax = "adb shell getprop [<prop_name>]",
            category = CommandCategory.ADVANCED_TWEAKS,
            titleAr = "قراءة خصائص ومعلومات عتاد الهاتف (Build Props)",
            descriptionAr = "يستعلم عن الخصائص الهندسية للجهاز: إصدار أندرويد، نوع المعالج، الشركة، ورقم البناء.",
            detailedExplanationAr = "يقرأ متغيرات ملف build.prop وخصائص نظام التشغيل. أمثلة لمفاتيح هامة:\n- ro.build.version.release: رقم إصدار أندرويد (مثال 14 أو 15)\n- ro.product.model: اسم وموديل الهاتف\n- ro.product.cpu.abi: معمارية المعالج (مثال arm64-v8a)\n- ro.boot.serialno: الرقم التسلسلي",
            example = "adb shell getprop ro.product.model",
            simulatedOutput = "Pixel 8 Pro",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "معرفة معمارية المعالج لتحميل حزم التطبيقات المناسبة",
                "التحقق من إصدار النظام الداخلي ورقم البناء بدقة"
            ),
            tags = listOf("getprop", "properties", "specs", "cpu", "model", "build")
        ),
        AdbCommand(
            id = "adb_shizuku_start",
            command = "adb shell sh /sdcard/Android/data/moe.shizuku.privileged.api/start.sh",
            syntax = "adb shell sh <shizuku_path>/start.sh",
            category = CommandCategory.ADVANCED_TWEAKS,
            titleAr = "تشغيل خدمة بيئة Shizuku",
            descriptionAr = "يبدأ تشغيل خدمة Shizuku التي تتيح للتطبيقات الأخرى استخدام صلاحيات ADB دون كمبيوتر.",
            detailedExplanationAr = "تطبيق Shizuku أحدث ثورة في تخصيص أندرويد، حيث يمنح التطبيقات الموثوقة واجهة برمجية لاستخدام أوامر ADB مباشرة على الهاتف. هذا الأمر يقوم بتشغيل خادم Shizuku في الخلفية.",
            example = "adb shell sh /sdcard/Android/data/moe.shizuku.privileged.api/start.sh",
            simulatedOutput = """info: shizuku_starter exit with 0
info: shizuku service started successfully!""",
            dangerLevel = DangerLevel.SAFE,
            commonUseCasesAr = listOf(
                "تشغيل خدمة Shizuku لتثبيت التطبيقات وإدارتها بدون كمبيوتر",
                "منح صلاحيات النظام للتطبيقات الداعمة"
            ),
            tags = listOf("shizuku", "service", "start", "tweaks")
        )
    )

    val tutorials: List<AdbTutorial> = listOf(
        AdbTutorial(
            id = "tut_what_is_adb",
            titleAr = "ما هو ADB وكيف يعمل؟",
            subtitleAr = "مفهوم جسر تصحيح أخطاء أندرويد وهيكلته",
            readTimeMinutes = 4,
            osTarget = "الجميع",
            iconName = "info",
            overviewAr = "جسر تصحيح أخطاء أندرويد (Android Debug Bridge - ADB) هو أداة سطر أوامر متعددة الاستخدامات تتيح لك التواصل المباشر مع جهاز أندرويد. يتكون ADB من ثلاثة أجزاء رئيسية تعمل معاً بتناغم وسلاسة.",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    titleAr = "العميل (Client)",
                    descriptionAr = "هو البرنامج الذي يعمل على جهاز الكمبيوتر الخاص بك. عند كتابة أي أمر يبدأ بـ 'adb' في سطر الأوامر (CMD أو Terminal)، فإنك تتحدث إلى العميل مباشرة."
                ),
                TutorialStep(
                    stepNumber = 2,
                    titleAr = "الخادم (Server)",
                    descriptionAr = "هو عملية تعمل في الخلفية على حاسوبك الشخصي على المنفذ المحلي 5037. وظيفته إدارة الاتصال بين العميل (Client) والبرنامج الخفي الذي يعمل على جهاز أندرويد."
                ),
                TutorialStep(
                    stepNumber = 3,
                    titleAr = "البرنامج الخفي (Daemon / adbd)",
                    descriptionAr = "هو خدمة تعمل بشكل دائم في خلفية نظام أندرويد على الهاتف نفسه. عندما تفعل 'تصحيح أخطاء USB'، فإنك تسمح لـ adbd باستقبال الأوامر وتنفيذها داخل نظام الهاتف."
                )
            ),
            importantNotesAr = listOf(
                "لا تحتاج لعمل روت (Root) لجهازك للاستفادة من معظم أوامر ومزايا ADB!",
                "ADB أداة رسمية مقدمة مباشرة من شركة Google ومضمنة في حزمة Android SDK الرسمية."
            )
        ),
        AdbTutorial(
            id = "tut_install_platform_tools",
            titleAr = "تثبيت أدوات ADB على الكمبيوتر",
            subtitleAr = "دليل التثبيت لـ Windows و Mac و Linux",
            readTimeMinutes = 6,
            osTarget = "Windows / Mac / Linux",
            iconName = "download",
            overviewAr = "لتشغيل أوامر ADB من أي مكان على حاسوبك، تحتاج لتحميل حزمة Platform-Tools الرسمية وإضافتها لمتغيرات البيئة (Environment Variables - PATH).",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    titleAr = "تحميل حزمة Android SDK Platform-Tools",
                    descriptionAr = "قم بتحميل الأرشيف الرسمي بصيغة ZIP من موقع Google Developer المخصص لنظامك (Windows أو Mac أو Linux)، ثم فك الضغط في مجلد دائم، مثلاً: C:\\platform-tools على ويندوز."
                ),
                TutorialStep(
                    stepNumber = 2,
                    titleAr = "إضافة المسار لمتغيرات البيئة (PATH) على Windows",
                    descriptionAr = "ابحث في قائمة ابدأ عن 'Environment Variables'، افتحها، اختر متغير 'Path' ثم انقر 'Edit'، ثم 'New' وأضف المسار الكامل للمجلد (C:\\platform-tools). اضغط OK.",
                    tipAr = "هذه الخطوة تتيح لك تشغيل adb من أي نافذة موجه أوامر في أي مسار."
                ),
                TutorialStep(
                    stepNumber = 3,
                    titleAr = "التثبيت على macOS و Linux بنقرة واحدة",
                    descriptionAr = "على نظام Mac يمكنك استخدام مدير الحزم Homebrew بتنفيذ الأمر:\nbrew install android-platform-tools\nوعلى أوبونتو/دبيان:\nsudo apt install adb fastboot",
                    commandSnippet = "brew install android-platform-tools"
                ),
                TutorialStep(
                    stepNumber = 4,
                    titleAr = "التحقق من نجاح التثبيت",
                    descriptionAr = "افتح نافذة Terminal أو CMD واكتب الأمر أدناه؛ إذا ظهر رقم الإصدار فهذا يعني أن التثبيت مكتمل بنجاح!",
                    commandSnippet = "adb version"
                )
            ),
            importantNotesAr = listOf(
                "إذا كان نظامك ويندوز 10 أو 11، ستحتاج أحياناً لتثبيت تعريفات Google USB Drivers إذا لم يتعرف النظام على هاتفك."
            )
        ),
        AdbTutorial(
            id = "tut_enable_usb_debugging",
            titleAr = "تفعيل تصحيح أخطاء USB على الهاتف",
            subtitleAr = "خطوات الوصول لخيارات المطور وقبول الاتصال",
            readTimeMinutes = 5,
            osTarget = "Android",
            iconName = "settings",
            overviewAr = "بشكل افتراضي لأسباب أمنية، تكون ميزة تصحيح أخطاء USB معطلة ومخفية في أندرويد. يجب تفعيلها لتتمكن من إرسال الأوامر لهاتفك.",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    titleAr = "إظهار قائمة خيارات المطور (Developer Options)",
                    descriptionAr = "افتح إعدادات الهاتف > حول الهاتف (About Phone) > معلومات البرنامج > اضغط 7 مرات متتالية وبسرعة على 'رقم الإصدار' (Build Number). سيظهر إشعار: 'أنت الآن مطور برامج!'."
                ),
                TutorialStep(
                    stepNumber = 2,
                    titleAr = "تفعيل تصحيح أخطاء USB (USB Debugging)",
                    descriptionAr = "ارجع لقائمة الإعدادات الرئيسية > النظام (أو الإعدادات الإضافية في شاومي وسامسونج) > اختر 'خيارات المطور' (Developer Options) > فعل مفتاح 'تصحيح أخطاء USB'."
                ),
                TutorialStep(
                    stepNumber = 3,
                    titleAr = "توصيل الكابل والموافقة على البصمة الرقمية (RSA Key)",
                    descriptionAr = "صل الهاتف بالكمبيوتر عبر كابل USB عالي الجودة. ستظهر نافذة منبثقة فورية على شاشة الهاتف تسألك: 'هل تريد السماح بتصحيح أخطاء USB من هذا الكمبيوتر؟' مع إظهار بصمة مفتاح RSA.",
                    tipAr = "ضع علامة صح في خيار 'السماح دائماً من هذا الكمبيوتر' ثم انقر موافق (Allow)."
                ),
                TutorialStep(
                    stepNumber = 4,
                    titleAr = "التأكد من جاهزية الاتصال",
                    descriptionAr = "نفذ الأمر أدناه على الكمبيوتر؛ يجب أن تشاهد كلمة 'device' بجوار الرقم التسلسلي لهاتفك.",
                    commandSnippet = "adb devices"
                )
            ),
            importantNotesAr = listOf(
                "في هواتف Xiaomi / Redmi (واجهة MIUI / HyperOS)، يجب تفعيل خياري: 'USB debugging' و 'USB debugging (Security settings)' لتتمكن من إعطاء الصلاحيات وتثبيت التطبيقات.",
                "استخدم كابل أصلي يدعم نقل البيانات وليس كابل شحن فقط."
            )
        ),
        AdbTutorial(
            id = "tut_wireless_debugging",
            titleAr = "التصحيح اللاسلكي بدون كابل (Wi-Fi)",
            subtitleAr = "طريقة الإقران لأندرويد 11+ والطريقة التقليدية",
            readTimeMinutes = 5,
            osTarget = "Android 11+",
            iconName = "wifi",
            overviewAr = "يمكنك العمل والتحكم بالهاتف دون الحاجة لربطه بكابل USB إطلاقاً، طالما أن الهاتف والكمبيوتر متصلان بنفس شبكة الواي فاي المحلية.",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    titleAr = "الطريقة الحديثة لأندرويد 11 فما فوق (Wireless Pairing)",
                    descriptionAr = "افتح إعدادات الهاتف > خيارات المطور > فعل 'التصحيح اللاسلكي' (Wireless Debugging) > انقر على الاسم نفسه للدخول > اختر 'إقران الجهاز برمز إقران' (Pair device with pairing code). ستظهر نافذة فيها عنوان IP ومنفذ ورمز سداسي الأرقام."
                ),
                TutorialStep(
                    stepNumber = 2,
                    titleAr = "تنفيذ أمر الإقران على الكمبيوتر",
                    descriptionAr = "في موجه الأوامر اكتب: adb pair <ip>:<port> ثم أدخل الرمز المكون من 6 أرقام عند طلبه.",
                    commandSnippet = "adb pair 192.168.1.50:37841"
                ),
                TutorialStep(
                    stepNumber = 3,
                    titleAr = "الاتصال بعد نجاح الإقران",
                    descriptionAr = "من نفس شاشة التصحيح اللاسلكي، انظر لعنوان IP والمنفذ المكتوبين في الأعلى تحت 'عنوان IP والمنفذ'، واكتب أمر الاتصال:",
                    commandSnippet = "adb connect 192.168.1.50:41253"
                ),
                TutorialStep(
                    stepNumber = 4,
                    titleAr = "الطريقة التقليدية لجميع إصدارات أندرويد السابقة",
                    descriptionAr = "صل الهاتف بالكابل مرة واحدة أولاً، ثم نفذ: adb tcpip 5555، بعدها افصل الكابل ونفذ: adb connect <هاتفك_IP>:5555."
                )
            )
        ),
        AdbTutorial(
            id = "tut_troubleshooting",
            titleAr = "حل أشهر مشاكل وأخطاء اتصال ADB",
            subtitleAr = "حل مشكلة Unauthorized و Device not found",
            readTimeMinutes = 5,
            osTarget = "استكشاف الأخطاء",
            iconName = "build",
            overviewAr = "قد تواجه بعض الرسائل الغامضة عند محاولة الاتصال بالهاتف، إليك الحلول المعتمدة والمجربة لكل حالة.",
            steps = listOf(
                TutorialStep(
                    stepNumber = 1,
                    titleAr = "حالة 'unauthorized' (غير مصرح)",
                    descriptionAr = "السبب: لم توافق بعد على شاشة الهاتف، أو أن إذن المفتاح انتهى. الحل: افتح شاشة الهاتف وابحث عن رسالة السماح واضغط OK. إذا لم تظهر، اذهب لخيارات المطور > اضغط 'إلغاء تفويضات تصحيح أخطاء USB' (Revoke USB debugging authorizations)، وافصل الكابل ثم أعد توصيله."
                ),
                TutorialStep(
                    stepNumber = 2,
                    titleAr = "القائمة فارغة تماماً عند كتابة 'adb devices'",
                    descriptionAr = "السبب: كابل تالف، أو منفذ USB فقط للشحن، أو نقص تعريفات USB Driver. الحل: جرب كابلاً آخر ومنفذ USB 2.0 خلفي على الحاسوب. في ويندوز، افتح Device Manager وتأكد أنه لا توجد علامة تعجب صفراء على اسم الهاتف."
                ),
                TutorialStep(
                    stepNumber = 3,
                    titleAr = "خطأ 'adb server version doesn't match this client'",
                    descriptionAr = "السبب: وجود برنامج محاكي (مثل BlueStacks أو LDPlayer) يشغل نسخة قديمة من ADB ويتعارض مع نسختك. الحل: أغلق المحاكي تماماً، ثم نفذ أمر قتل الخادم.",
                    commandSnippet = "adb kill-server && adb start-server"
                )
            )
        )
    )

    val quizQuestions: List<AdbQuizQuestion> = listOf(
        AdbQuizQuestion(
            id = 1,
            questionAr = "ما هو الأمر المستخدم لعرض قائمة الأجهزة المتصلة بحاسوبك وحالتها؟",
            optionsAr = listOf(
                "adb devices",
                "adb list",
                "adb show-all",
                "adb status"
            ),
            correctOptionIndex = 0,
            explanationAr = "أمر 'adb devices' هو الأمر القياسي لسرد جميع الأجهزة والمحاكيات المتصلة بخادم ADB المحلي."
        ),
        AdbQuizQuestion(
            id = 2,
            questionAr = "أي مفتاح (Flag) يضاف لأمر 'adb install' للسماح بإعادة التثبيت دون حذف بيانات التطبيق السابقة؟",
            optionsAr = listOf(
                "-f (force)",
                "-r (reinstall)",
                "-k (keep)",
                "-u (update)"
            ),
            correctOptionIndex = 1,
            explanationAr = "المفتاح -r يرمز إلى Reinstall، ويسمح بتحديث أو إعادة تثبيت ملف الـ APK مع الحفاظ التام على بيانات التطبيق الحالية."
        ),
        AdbQuizQuestion(
            id = 3,
            questionAr = "كيف يمكنك إعادة تشغيل الهاتف مباشرة إلى وضع الريكفري (Recovery Mode)؟",
            optionsAr = listOf(
                "adb restart -r",
                "adb reboot recovery",
                "adb shell reboot-recovery",
                "adb boot recovery"
            ),
            correctOptionIndex = 1,
            explanationAr = "الأمر 'adb reboot recovery' يوجه نظام أندرويد لإعادة الإقلاع الفوري داخل بارتشن الريكفري."
        ),
        AdbQuizQuestion(
            id = 4,
            questionAr = "ما معنى ظهور كلمة 'unauthorized' بجانب جهازك عند تنفيذ أمر 'adb devices'؟",
            optionsAr = listOf(
                "الهاتف لا يحتوي على صلاحية الروت (No Root)",
                "إصدار أندرويد قديم جداً وغير مدعوم",
                "لم تقم بالموافقة على نافذة تصحيح أخطاء USB على شاشة هاتفك بعد",
                "كابل الـ USB تالف"
            ),
            correctOptionIndex = 2,
            explanationAr = "تعني 'unauthorized' أن الهاتف لم يوافق بعد على مفتاح RSA العام للحاسوب من خلال النافذة المنبثقة على شاشة الهاتف."
        ),
        AdbQuizQuestion(
            id = 5,
            questionAr = "أي أمر يستخدم لسحب ملف من الهاتف وحفظه على جهاز الكمبيوتر؟",
            optionsAr = listOf(
                "adb push",
                "adb pull",
                "adb extract",
                "adb download"
            ),
            correctOptionIndex = 1,
            explanationAr = "أمر 'adb pull' يقوم بسحب وتحميل الملفات من الهاتف إلى الحاسوب، بينما 'adb push' يرسل الملفات من الحاسوب للهاتف."
        ),
        AdbQuizQuestion(
            id = 6,
            questionAr = "ما هو الأمر المستخدم لتعطيل تطبيقات النظام غير المرغوب فيها (Debloat) للمستخدم الحالي بدون روت؟",
            optionsAr = listOf(
                "adb shell pm disable-user --user 0 <package>",
                "adb uninstall --force-system <package>",
                "adb shell rm -rf /system/app/<package>",
                "adb system kill <package>"
            ),
            correctOptionIndex = 0,
            explanationAr = "أمر 'pm disable-user --user 0' يعطل حزمة التطبيق للمستخدم الأساسي دون الحاجة لصلاحيات الروت وبطريقة آمنة وقابلة للاستعادة."
        ),
        AdbQuizQuestion(
            id = 7,
            questionAr = "ما هي الأداة المستخدمة لعرض وتصفية سجلات النظام ورسائل تعطل التطبيقات (Crashes)؟",
            optionsAr = listOf(
                "adb dmesg",
                "adb logcat",
                "adb syslog",
                "adb console"
            ),
            correctOptionIndex = 1,
            explanationAr = "نظام Logcat في أندرويد مسؤول عن جمع وعرض جميع سجلات النظام والتطبيقات البرمجية عبر أمر 'adb logcat'."
        ),
        AdbQuizQuestion(
            id = 8,
            questionAr = "ما هو المنفذ الشبكي الافتراضي (Port) المستخدم لاتصالات ADB اللاسلكية؟",
            optionsAr = listOf(
                "8080",
                "5037",
                "5555",
                "22"
            ),
            correctOptionIndex = 2,
            explanationAr = "المنفذ 5555 هو المنفذ الافتراضي الذي تستمع عليه خدمة adbd على أجهزة أندرويد عند تفعيل وضع TCP/IP."
        ),
        AdbQuizQuestion(
            id = 9,
            questionAr = "كيف يمكنك محاكاة الضغط على زر الطاقة (Power Key) في الهاتف برمجياً؟",
            optionsAr = listOf(
                "adb shell input keyevent 26",
                "adb shell press power",
                "adb power off",
                "adb shell button 1"
            ),
            correctOptionIndex = 0,
            explanationAr = "الرمز 26 يمثل KEYCODE_POWER في نظام أندرويد لمحاكاة الضغط على زر التشغيل."
        ),
        AdbQuizQuestion(
            id = 10,
            questionAr = "أي أمر يعرض نسبة شحن البطارية وحرارتها وفولتيتها الحالية؟",
            optionsAr = listOf(
                "adb shell cat /proc/battery",
                "adb shell dumpsys battery",
                "adb battery info",
                "adb get battery"
            ),
            correctOptionIndex = 1,
            explanationAr = "خدمة 'dumpsys battery' توفر معلومات تفصيلية ودقيقة عن حالة بطارية أندرويد."
        )
    )
}
