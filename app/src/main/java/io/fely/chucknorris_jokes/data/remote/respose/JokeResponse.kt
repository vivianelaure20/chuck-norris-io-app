package io.fely.chucknorris_jokes.data.remote.respose

data class JokeResponse(
    var icon_url: String,
    var id: String,
    var url: String,
    var value: String
)
