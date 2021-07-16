package io.fely.chucknorris_jokes.di.viewmodel_scope

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.fely.chucknorris_jokes.data.remote.ChuckNorrisApi
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object MainModule {

    @Provides
    fun provideChuckNorrisApi(retrofit: Retrofit): ChuckNorrisApi = retrofit.create(ChuckNorrisApi::class.java)
}