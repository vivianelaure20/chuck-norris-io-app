package io.fely.chucknorris_jokes.data.remote.respose

data class JokeResponse(
    val icon_url: String,
    val id: String,
    val url: String,
    val value: String
)
