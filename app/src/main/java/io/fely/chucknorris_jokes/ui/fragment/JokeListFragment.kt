package io.fely.chucknorris_jokes.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import io.fely.chucknorris_jokes.R
import io.fely.chucknorris_jokes.data.local.model.JokeCategory
import io.fely.chucknorris_jokes.databinding.JokeListFragmentBinding
import io.fely.chucknorris_jokes.ui.MainViewModel
import io.fely.chucknorris_jokes.ui.adapter.JokeCategoryListAdapter
import io.fely.chucknorris_jokes.utils.NetworkResource
import io.fely.chucknorris_jokes.utils.setVisibility

class JokeListFragment: Fragment(R.layout.joke_list_fragment) {

    private val viewModel by activityViewModels<MainViewModel>()

    //views
    private var binding: JokeListFragmentBinding? =null
    private lateinit var jokeCategoryListView: RecyclerView
    private lateinit var categoryLstShimmerLayout: ShimmerFrameLayout
    private lateinit var jokeContent: TextView
    private lateinit var jokeCategoryDescription: TextView
    private lateinit var jokeCardScrollView: ScrollView
    private lateinit var jokeRequestProgressBar: ProgressBar
    private lateinit var nextJokeBtn: ImageButton



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
            jokeContent = it.jokeContent
            jokeCardScrollView = it.jokeDescriptionParent
            jokeRequestProgressBar = it.jokeRequestProgressbar
            jokeCategoryDescription = it.jokeDescription
            it.shuffleBtn.setOnClickListener{
                if(selectedCategoryName.isNotBlank()){
                    requestJoke(selectedCategoryName)
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
    }
    private fun observeGetJokeCategoryRequest(){
        viewModel.jokeCategoryRequestMutableStataFlow.removeObservers(viewLifecycleOwner)
        viewModel.jokeCategoryRequestMutableStataFlow.observe(viewLifecycleOwner){
            when(val value = it.getContentIfNotHandled()){
                is NetworkResource.Loading ->{
                    Log.d(TAG, "observeGetJokeCategoryRequest: *******Loading*********")
                    jokeCategoryListView.setVisibility(false)
                    categoryLstShimmerLayout.setVisibility(true)

                }
                is NetworkResource.Error ->{
                    Log.d(TAG, "observeGetJokeCategoryRequest: *******Error: ${value.exception}")
                    jokeCategoryListView.setVisibility(true)
                    categoryLstShimmerLayout.setVisibility(false)
                }
                is NetworkResource.Success -> {
                    Log.d(TAG, "observeGetJokeCategoryRequest: *******Success : ${value.data}")
                    //
                    if(value.data.isNotEmpty()){
//                        requestJoke(value.data[0].name)
                        jokeCategoryListView.setVisibility(true)
                        if(viewModel.loadInitialProduct){
                            viewModel.loadInitialProduct = false
                            viewModel.setSelectedProductCategory(value.data[0])
                        }
                    }
                    jokeCategoryAdapter.submitList(value.data)
                    categoryLstShimmerLayout.setVisibility(false)
                }
            }
        }
    }

    private fun observeJokeRequest(){
        viewModel.jokeRequestStateFlow.removeObservers(viewLifecycleOwner)
        viewModel.jokeRequestStateFlow.observe(viewLifecycleOwner){
            when(val value = it.getContentIfNotHandled()){
                is NetworkResource.Loading ->{
                    showJokeRequestProgressBar(true)
                }
                is NetworkResource.Error ->{
                    jokeContent.text = value.message
                    showJokeRequestProgressBar(false)
                }
                is NetworkResource.Success -> {

                    val text = "\" ${value.data.value} \""
                    jokeContent.text = text
                    showJokeRequestProgressBar(false)
                }
            }
        }
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

    private fun showJokeRequestProgressBar(show: Boolean){
        jokeRequestProgressBar.setVisibility(show)
        jokeCardScrollView.setVisibility(!show)
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