package droid.abdul.football.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import droid.abdul.football.BuildConfig
import droid.abdul.football.repository.api.ApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val networkModule = module {
    singleOf(::httpLoggingInterceptor)
    singleOf(::getOkHttpClient)
    singleOf(::getRetrofit)
    singleOf(::getJsonConverterFactory)
    single { createApiService<ApiService>(get()) }
}

fun httpLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoggingInterceptor
}


fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(getJsonConverterFactory())
        .client(getOkHttpClient(httpLoggingInterceptor()))
        .build()
}

private val json = Json {
    ignoreUnknownKeys = true
    ignoreUnknownKeys = true
    explicitNulls = false
}

fun getJsonConverterFactory(): Converter.Factory = json.asConverterFactory("application/json".toMediaType())


fun getOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
                .addHeader(
                    "Connection",
                    "close"
                )
                .addHeader(
                    "X-Auth-Token",
                    BuildConfig.X_AUTH_TOKEN
                )
                .build()
            chain.proceed(request)
        }
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()
}

inline fun <reified T> createApiService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}
