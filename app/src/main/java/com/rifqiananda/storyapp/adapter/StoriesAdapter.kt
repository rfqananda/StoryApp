package com.rifqiananda.storyapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.transition.platform.Hold
import com.rifqiananda.storyapp.databinding.LayoutAdapterBinding
import com.rifqiananda.storyapp.model.Story
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StoriesAdapter(val context: Context) : RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {

    private val listData = ArrayList<Story>()

    private var onItemClick: OnAdapterListener? = null

    fun setOnItemClick(onItemClick: OnAdapterListener){
        this.onItemClick = onItemClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Story>) {
        listData.clear()
        listData.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: LayoutAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesAdapter.ViewHolder {
        return ViewHolder(
            LayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: StoriesAdapter.ViewHolder, position: Int) {
        val storiesData = listData[position]
        holder.binding.apply {
            Glide.with(context)
                .load(storiesData.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivPhoto)

            tvName.text = storiesData.name
            tvDescription.text = storiesData.description

            val date = storiesData.createdAt
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val newDate = format.parse(date)
            val format2 = SimpleDateFormat("yyyy-MM-dd")

            tvDate.text = format2.format(newDate)

            btnDetail.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        btnDetail.context as Activity,
                        Pair(ivPhoto, "photo")
                    )
                onItemClick?.onClick(storiesData, optionsCompat)

            }
        }
    }

    override fun getItemCount(): Int = listData.size

    interface OnAdapterListener {
        fun onClick(data: Story, option: ActivityOptionsCompat)
    }

}