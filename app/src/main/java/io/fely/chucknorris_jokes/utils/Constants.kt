package io.fely.chucknorris_jokes.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

object Constants {

    const val BASE_URL = "https://api.chucknorris.io/jokes"


}