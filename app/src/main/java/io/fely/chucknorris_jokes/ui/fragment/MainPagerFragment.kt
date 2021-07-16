package io.fely.chucknorris_jokes.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.fely.chucknorris_jokes.R
import io.fely.chucknorris_jokes.databinding.MainViewPagerFreagmentBinding

@AndroidEntryPoint
class MainPagerFragment: Fragment(R.layout.main_view_pager_freagment) {

    private var _binding: MainViewPagerFreagmentBinding? = null

    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = MainViewPagerFreagmentBinding.bind(view)



    }
}