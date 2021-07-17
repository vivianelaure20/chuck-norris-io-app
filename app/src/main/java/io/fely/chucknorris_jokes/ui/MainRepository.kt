package io.fely.chucknorris_jokes.ui

import android.util.Log
import io.fely.chucknorris_jokes.data.local.model.JokeCategory
import io.fely.chucknorris_jokes.data.remote.ChuckNorrisApi
import io.fely.chucknorris_jokes.data.remote.respose.JokeResponse
import io.fely.chucknorris_jokes.utils.NetworkResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


class MainRepository @Inject constructor(
    private val chuckNorrisApi: ChuckNorrisApi
){

    suspend fun getJokeCategoryList() : Flow<NetworkResource<List<JokeCategory>>> = flow{
        try{
            val response = chuckNorrisApi.getJokeCategoryList()
            val categoryList = mutableListOf<JokeCategory>()

            response.forEachIndexed { index, value ->
                categoryList.add(JokeCategory(index+1, value))
            }

            emit(NetworkResource.Success(categoryList))
        }catch (e: Exception){
            Log.d(TAG, "getJokeCategoryList: Error: $e")
            emit(NetworkResource.Error(e, "${e.message}"))
        }

    }

    suspend fun getJoke(query: String) : Flow<NetworkResource<JokeResponse>> = flow{
        try{
            val response = chuckNorrisApi.getJoke(query)
            Log.d(TAG, "getJoke: Response: $response")
            emit(NetworkResource.Success(response))
        }catch (e: Exception){
            Log.d(TAG, "getJokeCategoryList: Error: $e")
            emit(NetworkResource.Error(e, "${e.message}"))
        }

    }

    companion object {
        private const val TAG = "MainRepository"
    }

}