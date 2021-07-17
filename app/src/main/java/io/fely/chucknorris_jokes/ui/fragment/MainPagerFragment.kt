package io.fely.chucknorris_jokes.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.fely.chucknorris_jokes.R
import io.fely.chucknorris_jokes.databinding.MainViewPagerFreagmentBinding
import io.fely.chucknorris_jokes.ui.MainViewModel
import io.fely.chucknorris_jokes.ui.adapter.ViewPagerFragmentAdapter
import io.fely.chucknorris_jokes.ui.pager_transfomer.ZoomOutPageTransformer
import io.fely.chucknorris_jokes.utils.reduceDragSensitivity

@AndroidEntryPoint
class MainPagerFragment: Fragment(R.layout.main_view_pager_freagment) {

    private var _binding: MainViewPagerFreagmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout : TabLayout

    private val viewModel by activityViewModels<MainViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = MainViewPagerFreagmentBinding.bind(view)
        setupViewPager()
    }

    private fun setupViewPager(){
        val fragmentList = listOf(
            JokeListFragment(),
            RandomJokeFragment(),
        )
        viewPager = binding.pager
        tabLayout = binding.tabLayout
        val viewPagerAdapter  = ViewPagerFragmentAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        viewPager.adapter = viewPagerAdapter
        viewPager.setPageTransformer(ZoomOutPageTransformer())
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        viewPager.reduceDragSensitivity()
        attachViewPager()
    }

    private fun attachViewPager(){
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            when(position){
                0 -> {
                    tab.text = "Joke CategoryList"
                }
                1 -> {
                    tab.text = "Random Joke"
                }
            }
        }.attach()
    }


    private var onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (position == 0){
                viewModel.requestJokeCategoryList()
            }else{
                viewModel.requestRandomJoke()
            }
        }
    }

}