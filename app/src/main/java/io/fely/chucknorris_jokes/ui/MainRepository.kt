package io.fely.chucknorris_jokes.ui

import io.fely.chucknorris_jokes.data.local.model.JokeCategory
import io.fely.chucknorris_jokes.data.remote.ChuckNorrisApi
import io.fely.chucknorris_jokes.data.remote.respose.JokeResponse
import io.fely.chucknorris_jokes.utils.NetworkResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val chuckNorrisApi: ChuckNorrisApi
){

    /**
     * The method requests list of jokes category
     */
    suspend fun getJokeCategoryList() : Flow<NetworkResource<List<JokeCategory>>> = flow{
        try{
            val response = chuckNorrisApi.getJokeCategoryList()
            val categoryList = mutableListOf<JokeCategory>()

            response.forEachIndexed { index, value ->
                categoryList.add(JokeCategory(index+1, value))
            }
            emit(NetworkResource.Success(categoryList))
        }catch (e: Exception){
            emit(NetworkResource.Error(e, ERROR_MESSAGE))
        }

    }

    /**
     * Request joke:  the method make api request based on the query parameter
     */
    suspend fun getJoke(query: String = "") : Flow<NetworkResource<JokeResponse>> = flow{
        try{
            val response : JokeResponse = if(query.isBlank()){
                chuckNorrisApi.getJokeRandom()
            }else{
                chuckNorrisApi.getJoke(query)
            }
            emit(NetworkResource.Success(response))

        }catch (e: Exception){
            emit(NetworkResource.Error(e, ERROR_MESSAGE))
        }

    }

    companion object {
        const val ERROR_MESSAGE: String = "Sorry, failed to connect, please try again or check your internet"
    }

}