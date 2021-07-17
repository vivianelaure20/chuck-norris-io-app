package io.fely.chucknorris_jokes.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText
import io.fely.chucknorris_jokes.R
import io.fely.chucknorris_jokes.databinding.JokeListFragmentBinding
import io.fely.chucknorris_jokes.databinding.RandomJokeFragmentBinding
import io.fely.chucknorris_jokes.ui.MainViewModel
import io.fely.chucknorris_jokes.utils.NetworkResource
import io.fely.chucknorris_jokes.utils.observeJokeRequest
import io.fely.chucknorris_jokes.utils.setVisibility

class RandomJokeFragment: Fragment(R.layout.random_joke_fragment) {

    private val viewModel by activityViewModels<MainViewModel>()


    private lateinit var jokeContent: TextView
    private lateinit var jokeCardScrollView: NestedScrollView
    private lateinit var jokeRequestProgressBar: ProgressBar



    private var binding: RandomJokeFragmentBinding? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = RandomJokeFragmentBinding.bind(view)
        initViews()
        observeJokeRequest()
    }

    private fun initViews() {
        binding?.let {
            jokeContent = it.randomJokeCardLayout.jokeContent
            jokeCardScrollView = it.randomJokeCardLayout.jokeDescriptionParent
            jokeRequestProgressBar = it.randomJokeCardLayout.jokeRequestProgressbar
            it.randomJokeCardLayout.shuffleBtn.setOnClickListener{
                requestJoke()
            }
        }
    }

    private fun requestJoke() {
        viewModel.requestRandomJoke()
    }


    private fun observeJokeRequest(){
        binding?.randomJokeCardLayout?.observeJokeRequest(
            viewModel.randomJokeRequestStateFlow,
            viewLifecycleOwner
        )
    }

    private fun showJokeRequestProgressBar(show: Boolean){
        jokeRequestProgressBar.setVisibility(show)
        jokeCardScrollView.setVisibility(!show)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RandomJokeFragment()
    }
}