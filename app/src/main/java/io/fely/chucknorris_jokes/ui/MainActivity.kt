package io.fely.chucknorris_jokes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.fely.chucknorris_jokes.R
import io.fely.chucknorris_jokes.utils.NetworkResource
import com.microsoft.appcenter.crashes.Crashes

import com.microsoft.appcenter.analytics.Analytics

import com.microsoft.appcenter.AppCenter




@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCenter.start(
            application, "d86a129d-2a95-41c5-a200-937f88ede39f",
            Analytics::class.java, Crashes::class.java
        )
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}