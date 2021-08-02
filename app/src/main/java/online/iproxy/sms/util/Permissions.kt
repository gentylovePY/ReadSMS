package online.iproxy.sms.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object Permissions {

    private val REQUIRED_PERMISSIONS = listOf(Manifest.permission.RECEIVE_SMS)

    fun shouldShowRequestPermissionsRationale(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        return REQUIRED_PERMISSIONS.any { permission ->
            activity.shouldShowRequestPermissionRationale(permission)
        }
    }

    fun getMissingPermissions(context: Context): List<String> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return emptyList()
        }
        return REQUIRED_PERMISSIONS.filter { permission ->
            context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
        }
    }

}