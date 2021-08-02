package online.iproxy.sms.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object ImplicitIntents {
    /**
     * Opens app Settings screen for a given app [packageName].
     */
    fun appSettingsIntent(context: Context, packageName: String = context.packageName): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        return intent
    }
}