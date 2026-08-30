package com.zhenya.dpichanger

import android.content.Context
import android.os.IBinder
import android.os.UserHandle

object DensityUtils {

    private fun getWindowManagerService(): Any {
        val serviceManagerClass = Class.forName("android.os.ServiceManager")
        val getService = serviceManagerClass.getMethod("getService", String::class.java)
        val binder = getService.invoke(null, "window") as IBinder

        val stubClass = Class.forName("android.view.IWindowManager\$Stub")
        val asInterface = stubClass.getMethod("asInterface", IBinder::class.java)
        return asInterface.invoke(null, binder)!!
    }

    fun getCurrentDensity(context: Context): Int {
        return context.resources.displayMetrics.densityDpi
    }

    fun getDefaultDensity(context: Context): Int {
        return try {
            val displayMetricsClass = Class.forName("android.util.DisplayMetrics")
            val field = displayMetricsClass.getField("DENSITY_DEVICE_STABLE")
            field.getInt(null)
        } catch (e: Exception) {
            context.resources.displayMetrics.densityDpi
        }
    }

    fun setDensity(density: Int): Result<Unit> {
        return try {
            val wm = getWindowManagerService()
            val method = wm.javaClass.getMethod(
                "setForcedDisplayDensityForUser",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            method.invoke(wm, density, UserHandle.myUserId())
            Result.success(Unit)
        } catch (e: SecurityException) {
            Result.failure(Exception("Нет прав WRITE_SECURE_SETTINGS. Выдай через ADB.", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun resetDensity(): Result<Unit> {
        return try {
            val wm = getWindowManagerService()
            val method = wm.javaClass.getMethod(
                "clearForcedDisplayDensityForUser",
                Int::class.javaPrimitiveType
            )
            method.invoke(wm, UserHandle.myUserId())
            Result.success(Unit)
        } catch (e: SecurityException) {
            Result.failure(Exception("Нет прав WRITE_SECURE_SETTINGS. Выдай через ADB.", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
