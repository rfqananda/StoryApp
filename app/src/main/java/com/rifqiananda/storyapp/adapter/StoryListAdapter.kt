package com.rifqiananda.storyapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rifqiananda.storyapp.databinding.LayoutAdapterBinding
import com.rifqiananda.storyapp.model.Story
import java.text.SimpleDateFormat

class StoryListAdapter(val context: Context) : PagingDataAdapter<Story, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClick: OnAdapterListener? = null

    fun setOnItemClick(onItemClick: OnAdapterListener){
        this.onItemClick = onItemClick
    }

    class MyViewHolder(val binding: LayoutAdapterBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = LayoutAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, parent.context)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = getItem(position)
        if (data != null) {
            holder.binding.apply {
                Glide.with(context)
                    .load(data.photoUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .transform(CenterCrop(), RoundedCorners(60))
                    .into(ivPhoto)

                tvName.text = data.name

                val date = data.createdAt
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val newDate = format.parse(date)
                val format2 = SimpleDateFormat("yyyy-MM-dd")

                tvDate.text = newDate?.let { format2.format(it) }

                holder.itemView.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            Pair(ivPhoto, "photo")
                        )
                    onItemClick?.onClick(data, optionsCompat)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface OnAdapterListener {
        fun onClick(data: Story, option: ActivityOptionsCompat)
    }
}

