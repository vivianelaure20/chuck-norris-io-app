package io.fely.chucknorris_jokes.data.remote

import io.fely.chucknorris_jokes.data.remote.respose.JokeResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ChuckNorrisApi {

    @GET("random")
    suspend fun getJoke(@Query("category") query: String): JokeResponse

    @GET("categories")
    suspend fun getJokeCategoryList(): List<String>

//    @GET("random")
//    fun getJokeCategory(@Query("category") category: String): JokeResponse

}