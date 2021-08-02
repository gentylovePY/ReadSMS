package online.iproxy.sms.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import online.iproxy.sms.BuildConfig
import online.iproxy.sms.data.api.AuthApi
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val ENDPOINT_URL = "https://iproxy.online/api/"

    @Provides
    @Singleton
    fun provideAuthApi(
        httpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): AuthApi {
        return Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .client(httpClient)
            .baseUrl(ENDPOINT_URL)
            .validateEagerly(BuildConfig.DEBUG)
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Reusable
    @OptIn(ExperimentalSerializationApi::class)
    fun provideConverterFactory(json: Json): Converter.Factory {
        val contentType = "application/json".toMediaType()
        return json.asConverterFactory(contentType)
    }

}