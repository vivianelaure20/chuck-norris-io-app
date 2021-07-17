package io.fely.chucknorris_jokes.utils

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import io.fely.chucknorris_jokes.R
import io.fely.chucknorris_jokes.data.remote.respose.JokeResponse
import io.fely.chucknorris_jokes.databinding.JokeCardLayoutBinding


fun View.setVisibility(isVisible: Boolean){
    visibility = if(isVisible)
        View.VISIBLE
    else
        View.GONE
}

fun JokeCardLayoutBinding.observeJokeRequest(requestLiveData: LiveData<Event<NetworkResource<JokeResponse>>>, lifecycleOwner: LifecycleOwner){
    requestLiveData.removeObservers(lifecycleOwner)
    requestLiveData.observe(lifecycleOwner){response ->
        when(val value = response.getContentIfNotHandled()){
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
                jokeIcon.load(value.data.icon_url)
                showJokeRequestProgressBar(false)
            }
        }
    }
}

fun ImageView.load(imageAddress: String) {
    Glide.with(context)
        .load(imageAddress)
        .placeholder(R.drawable.ic_launcher_background)
        .into(this)
}

private fun JokeCardLayoutBinding.showJokeRequestProgressBar(show: Boolean){
    jokeRequestProgressbar.setVisibility(show)
    jokeDescriptionParent.setVisibility(!show)
}


fun ViewPager2.reduceDragSensitivity() {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this) as RecyclerView

    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView) as Int
    touchSlopField.set(recyclerView, touchSlop*4)       // "8" was obtained experimentally
}


