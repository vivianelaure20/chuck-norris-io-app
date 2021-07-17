package io.fely.chucknorris_jokes.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import io.fely.chucknorris_jokes.R
import io.fely.chucknorris_jokes.ui.MainViewModel

class RandomJokeFragment: Fragment(R.layout.random_joke_fragment) {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RandomJokeFragment()
    }
}