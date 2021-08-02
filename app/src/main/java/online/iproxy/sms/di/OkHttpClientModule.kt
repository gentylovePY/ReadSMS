package online.iproxy.sms.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import online.iproxy.sms.BuildConfig
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpClientModule {

    private const val CONNECT_TIMEOUT_MS = 25_000L
    private const val IO_TIMEOUT_MS = 35_000L

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .readTimeout(IO_TIMEOUT_MS, TimeUnit.MILLISECONDS)

        // todo: add AuthInterceptor here

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor())
        } else {
            OkHttpClient.Builder().build()
        }

        return builder.build()
    }

}

