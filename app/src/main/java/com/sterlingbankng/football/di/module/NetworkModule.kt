package com.sterlingbankng.football.di.module

import com.sterlingbankng.football.repository.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { httpLoggingInterceptor() }
    single { getOkHttpClient(get()) }
    single { getRetrofit() }
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
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClient(httpLoggingInterceptor()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}


fun getOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor { chain ->
            val request = chain.request().newBuilder().addHeader(
                "Connection",
                "close"
            ).build()
            chain.proceed(request)
        }
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()
}

inline fun <reified T> createApiService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}
