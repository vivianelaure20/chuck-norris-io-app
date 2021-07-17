package io.fely.chucknorris_jokes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.fely.chucknorris_jokes.data.local.model.JokeCategory
import io.fely.chucknorris_jokes.data.remote.respose.JokeResponse
import io.fely.chucknorris_jokes.utils.Event
import io.fely.chucknorris_jokes.utils.NetworkResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel(){

    private val _jokeRequestStateFlow = MutableStateFlow<Event<NetworkResource<JokeResponse>>>(Event(NetworkResource.Empty))
    val jokeRequestStateFlow = _jokeRequestStateFlow.asLiveData()

    private val _jokeCategoryRequestMutableStataFlow = MutableStateFlow<Event<NetworkResource<List<JokeCategory>>>>(Event(NetworkResource.Empty))
    val jokeCategoryRequestMutableStataFlow = _jokeCategoryRequestMutableStataFlow.asLiveData()

    fun requestJokeCategoryList() = viewModelScope.launch{
        _jokeCategoryRequestMutableStataFlow.value = Event(NetworkResource.Loading)
        mainRepository.getJokeCategoryList().collect {
            _jokeCategoryRequestMutableStataFlow.value = Event(it)
        }
    }

    fun requestJoke(query: String = "") = viewModelScope.launch{
        _jokeRequestStateFlow.value = Event(NetworkResource.Loading)
       if(query.isBlank()){
           mainRepository.getJoke("").collect {
               _jokeRequestStateFlow.value = Event(it)
           }
       }else{
           mainRepository.getJoke(query).collect {
               _jokeRequestStateFlow.value = Event(it)
           }
       }
    }

}