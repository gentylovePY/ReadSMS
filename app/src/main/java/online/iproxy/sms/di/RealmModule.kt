package online.iproxy.sms.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.RealmConfiguration
import online.iproxy.sms.data.db.SmsAppRealmSchema
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {

    @Provides
    @Singleton
    fun provideRealmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder()
            .name("iproxy-sms.realm")
            .modules(SmsAppRealmSchema())
            .allowQueriesOnUiThread(false)
            .schemaVersion(1)
            .compactOnLaunch()
            .deleteRealmIfMigrationNeeded() // todo: remove after released to production
            .build()
    }

}
