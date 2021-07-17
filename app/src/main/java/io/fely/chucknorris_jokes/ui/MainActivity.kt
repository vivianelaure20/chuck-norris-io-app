package io.fely.chucknorris_jokes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.fely.chucknorris_jokes.R
import io.fely.chucknorris_jokes.utils.NetworkResource

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeGetJokeCategoryRequest()
        observeJokeRequest()
        viewModel.requestJokeCategoryList()

    }

    private fun observeGetJokeCategoryRequest(){
        viewModel.jokeCategoryRequestMutableStataFlow.removeObservers(this)
        viewModel.jokeCategoryRequestMutableStataFlow.observe(this){
            when(val value = it.getContentIfNotHandled()){
                is NetworkResource.Loading ->{
                    Log.d(TAG, "observeGetJokeCategoryRequest: *******Loading*********")
                }
                is NetworkResource.Error ->{
                    Log.d(TAG, "observeGetJokeCategoryRequest: *******Error: ${value.exception}")
                }
                is NetworkResource.Success -> {
                    Log.d(TAG, "observeGetJokeCategoryRequest: *******Success : ${value.data}")
                    requestJoke(value.data[0].name)
                }
            }
        }
    }

    private fun observeJokeRequest(){
        viewModel.jokeRequestStateFlow.removeObservers(this)
        viewModel.jokeRequestStateFlow.observe(this){
            when(val value = it.getContentIfNotHandled()){
                is NetworkResource.Loading ->{
                    Log.d(TAG, "observeJokeRequest: *******Loading*********")
                }
                is NetworkResource.Error ->{
                    Log.d(TAG, "observeJokeRequest: *******Error: ${value.exception}")
                }
                is NetworkResource.Success -> {
                    Log.d(TAG, "observeJokeRequest: *******Success : ${value.data}")
                }
            }
        }
    }

    private fun requestJoke(query: String){
        viewModel.requestJoke(query)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}