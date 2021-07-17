package io.fely.chucknorris_jokes.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import io.fely.chucknorris_jokes.R
import io.fely.chucknorris_jokes.data.local.model.JokeCategory
import io.fely.chucknorris_jokes.databinding.JokeListFragmentBinding
import io.fely.chucknorris_jokes.ui.MainViewModel
import io.fely.chucknorris_jokes.ui.adapter.JokeCategoryListAdapter
import io.fely.chucknorris_jokes.utils.NetworkResource
import io.fely.chucknorris_jokes.utils.observeJokeRequest
import io.fely.chucknorris_jokes.utils.setVisibility

class JokeListFragment: Fragment(R.layout.joke_list_fragment) {

    private val viewModel by activityViewModels<MainViewModel>()

    //views
    private var binding: JokeListFragmentBinding? =null
    private lateinit var jokeCategoryListView: RecyclerView
    private lateinit var categoryLstShimmerLayout: ShimmerFrameLayout
    private lateinit var jokeContent: TextView
    private lateinit var jokeCategoryDescription: TextView
    private lateinit var jokeCardScrollView: NestedScrollView
    private lateinit var jokeRequestProgressBar: ProgressBar



    private var  selectedCategoryName: String = ""



    private lateinit var jokeCategoryAdapter: JokeCategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = JokeListFragmentBinding.bind(view)
        initViews()
        observeGetJokeCategoryRequest()
        observeJokeRequest()
        observeSelectedJokeCategory()

    }
    private fun initViews(){
        binding?.let {
            jokeCategoryListView = it.jokeCategoryListview
            categoryLstShimmerLayout = it.itemCategoryShimmerLayout
            jokeContent = it.jokeCardLayout.jokeContent
            jokeCardScrollView = it.jokeCardLayout.jokeDescriptionParent
            jokeRequestProgressBar = it.jokeCardLayout.jokeRequestProgressbar
            jokeCategoryDescription = it.jokeDescription
            it.jokeCardLayout.shuffleBtn.setOnClickListener{
                if(selectedCategoryName.isNotBlank()){
                    requestJoke(selectedCategoryName)
                }else{
                    viewModel.requestJokeCategoryList()
                }
            }
        }
        setJokeCategoryListView()
    }

    private fun setJokeCategoryListView(){
        jokeCategoryListView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        jokeCategoryAdapter = JokeCategoryListAdapter(::onCategoryClicked,viewModel.selectedJokeCategoryStateFlow, viewLifecycleOwner)

        jokeCategoryListView.adapter = jokeCategoryAdapter

    }


    private fun onCategoryClicked(jokeCategory: JokeCategory, position: Int){
        viewModel.setSelectedProductCategory(jokeCategory)
        viewModel.selectedPosition = position
    }
    private fun observeGetJokeCategoryRequest(){
        viewModel.jokeCategoryRequestMutableStataFlow.removeObservers(viewLifecycleOwner)
        viewModel.jokeCategoryRequestMutableStataFlow.observe(viewLifecycleOwner){
            when(val value = it.getContentIfNotHandled()){
                is NetworkResource.Loading ->{
                    setShimmerVisibility(true)
                }
                is NetworkResource.Error ->{
                    binding?.jokeCardLayout?.jokeContent?.text = value.message
                    setShimmerVisibility(false)
                }
                is NetworkResource.Success -> {
                    if(value.data.isNotEmpty()){
                        if(viewModel.loadInitialProduct){
                            viewModel.selectedPosition = 0
                            viewModel.loadInitialProduct = false
                            viewModel.setSelectedProductCategory(value.data[0])
                        }
                    }
                    val position = viewModel.selectedPosition
                    jokeCategoryListView.scrollToPosition(position)
                    jokeCategoryAdapter.submitList(value.data)
                    setShimmerVisibility(false)
                }
            }
        }
    }

    private fun setShimmerVisibility(show: Boolean){
        categoryLstShimmerLayout.setVisibility(show)
        jokeCategoryListView.setVisibility(!show)
    }

    private fun observeJokeRequest(){
        binding?.jokeCardLayout?.observeJokeRequest(
            viewModel.jokeRequestStateFlow,
            viewLifecycleOwner
        )
    }



    private fun observeSelectedJokeCategory(){
        viewModel.selectedJokeCategoryStateFlow.removeObservers(viewLifecycleOwner)
        viewModel.selectedJokeCategoryStateFlow.observe(viewLifecycleOwner){
            if(isAdded){
                it?.let {
                    selectedCategoryName = it.name
                    val description = "Random joke  about \" ${it.name} \", click next arrow to get the next joke"
                    jokeCategoryDescription.text = description
                    requestJoke(it.name)
                }
            }
        }
    }

    private fun requestJoke(query: String){
        viewModel.requestJoke(query)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val TAG = "JokeListFragment"
        @JvmStatic
        fun newInstance() =
            JokeListFragment()
    }
}