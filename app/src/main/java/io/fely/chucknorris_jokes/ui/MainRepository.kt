package io.fely.chucknorris_jokes.ui

import io.fely.chucknorris_jokes.data.remote.ChuckNorrisApi
import javax.inject.Inject
import javax.inject.Singleton


class MainRepository @Inject constructor(
    private val chuckNorrisApi: ChuckNorrisApi
){

}