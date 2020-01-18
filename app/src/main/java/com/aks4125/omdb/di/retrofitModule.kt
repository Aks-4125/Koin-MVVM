package com.aks4125.omdb.di

import android.content.Context
import com.aks4125.omdb.network.ApiConstants
import com.aks4125.omdb.network.WebService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


val retrofitModule = module {

    single { getApiInterface(get()) }
    single { getRetrofit(get()) }
    single { getOkHttpClient(get(), get()) }
    single { getCache(get()) }
    single { getHttpLoginInteractor() }
}


fun getApiInterface(retrofit: Retrofit): WebService {
    return retrofit.create(WebService::class.java)
}

fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

fun getOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .cache(cache)
        .build()

}

fun getHttpLoginInteractor(): HttpLoggingInterceptor {
    val httpLoginInterceptor = HttpLoggingInterceptor()
    httpLoginInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoginInterceptor
}

fun getCache(context: Context): Cache {
    val file = File(context.cacheDir, "responses")
    return Cache(file, 10 * 1024 * 1024)
}