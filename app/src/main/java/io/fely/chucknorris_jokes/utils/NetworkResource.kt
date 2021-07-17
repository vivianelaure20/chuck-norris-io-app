package io.fely.chucknorris_jokes.utils

import java.lang.Exception

sealed class NetworkResource<out R>{
    data class Success<out T>(val data: T) : NetworkResource<T>()
    data class Error(val exception: Exception, val message: String) : NetworkResource<Nothing>()
    object Empty : NetworkResource<Nothing>()
    object Loading : NetworkResource<Nothing>()
}
