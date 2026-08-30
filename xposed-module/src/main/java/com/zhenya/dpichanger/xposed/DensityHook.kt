package com.zhenya.dpichanger.xposed

import android.app.Application
import android.content.res.Configuration
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class DensityHook : IXposedHookLoadPackage {

    private val TARGET_PACKAGES = setOf(
        "com.example.targetapp"
    )

    private val TARGET_DENSITY = 160

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        if (lpparam.packageName !in TARGET_PACKAGES) return

        XposedHelpers.findAndHookMethod(
            Application::class.java,
            "attach",
            android.content.Context::class.java,
            object : de.robv.android.xposed.XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val app = param.thisObject as Application
                    overrideDensity(app)
                }
            }
        )
    }

    private fun overrideDensity(app: Application) {
        try {
            val resources = app.resources
            val config = Configuration(resources.configuration)
            config.densityDpi = TARGET_DENSITY

            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)

            resources.displayMetrics.densityDpi = TARGET_DENSITY
            resources.displayMetrics.density = TARGET_DENSITY / 160f
        } catch (e: Throwable) {
            de.robv.android.xposed.XposedBridge.log("DensityHook error: ${e.message}")
        }
    }
}
