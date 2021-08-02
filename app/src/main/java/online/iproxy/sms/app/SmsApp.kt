package online.iproxy.sms.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import online.iproxy.sms.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class SmsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Realm.init(this)
    }

}