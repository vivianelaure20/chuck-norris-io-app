package io.fely.chucknorris_jokes.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import io.fely.chucknorris_jokes.R
import io.fely.chucknorris_jokes.data.local.model.JokeCategory
import io.fely.chucknorris_jokes.data.remote.respose.JokeResponse
import io.fely.chucknorris_jokes.databinding.JokeCategoryItemBinding

class JokeCategoryListAdapter(
    private val onItemClick: (JokeCategory) -> Unit,
    private val selectedItem: LiveData<JokeCategory?>,
    private val lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JokeCategory>() {

        override fun areItemsTheSame(oldItem: JokeCategory, newItem: JokeCategory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: JokeCategory, newItem: JokeCategory): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = JokeCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JokeCategoryListViewHolder(
            binding,
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is JokeCategoryListViewHolder -> {
                val item = differ.currentList[position]
                holder.bind(item)
                selectedItem.observe(lifecycleOwner){
                    if(it != null){
                        if( it.id == item.id){
                            holder.setItemBorder(true)
                        }else{
                            holder.setItemBorder(false)
                        }
                    }
                }
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<JokeCategory>) {
        differ.submitList(list)
    }

    class JokeCategoryListViewHolder
    constructor(
        private val itemBinding: JokeCategoryItemBinding,
        private val onItemClick: (JokeCategory) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private val nameTextView: AppCompatTextView = itemBinding.jokeCategoryItemName
        private val itemParent: ConstraintLayout = itemBinding.jokeCategoryItemParent
        private val itemViewLayout: ConstraintLayout = itemBinding.jokeCategoryItemLayout


        fun bind(item: JokeCategory) = with(itemView) {
            itemBinding.root.setOnClickListener {
                onItemClick.invoke(item)
            }

            itemBinding.jokeCategoryItemName.text = item.name
        }

        fun setItemBorder(isSelected: Boolean){
            if(isSelected){
                itemParent.setBackgroundResource(R.drawable.circle_shape_with_border_primary_color)
                nameTextView.setTextColor(ContextCompat.getColor(nameTextView.context, R.color.backgroundColor))
                itemViewLayout.setBackgroundColor(ContextCompat.getColor(itemViewLayout.context, R.color.blue_600))
            }else{

                itemParent.setBackgroundResource(R.drawable.joke_category_circle_shape_with_border_unselect_item)
                nameTextView.setTextColor(ContextCompat.getColor(nameTextView.context, R.color.black_900))
                itemViewLayout.setBackgroundColor(ContextCompat.getColor(itemViewLayout.context, R.color.blue_100))
            }
        }

    }
}