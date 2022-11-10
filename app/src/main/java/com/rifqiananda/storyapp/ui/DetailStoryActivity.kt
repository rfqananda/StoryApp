package com.rifqiananda.storyapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rifqiananda.storyapp.PhotoDialog
import com.rifqiananda.storyapp.databinding.ActivityDetailStoryBinding
import java.text.SimpleDateFormat

class DetailStoryActivity : AppCompatActivity() {

    companion object {
        const val USER_PHOTO = "user_photo"
        const val USER_NAME = "user_name"
        const val USER_DESCRIPTION = "user_description"
        const val USER_CREATED = "user_created"
    }

    private lateinit var seePhoto: PhotoDialog

    private lateinit var binding : ActivityDetailStoryBinding

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        seePhoto = PhotoDialog(this)

        binding.apply {
            Glide.with(this@DetailStoryActivity)
                .load(intent.getStringExtra(USER_PHOTO))
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(CenterCrop(), RoundedCorners(60))
                .into(ivPhoto)

            tvName.text = intent.getStringExtra(USER_NAME)
            tvDescription.text = intent.getStringExtra(USER_DESCRIPTION)

            val createdDate = intent.getStringExtra(USER_CREATED)
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val dateData = format.parse(createdDate!!)
            val formatDate = SimpleDateFormat("yyyy-MM-dd")
            val formatTime = SimpleDateFormat("HH:mm")
            val date = formatDate.format(dateData!!)
            val time = formatTime.format(dateData!!)

            tvDate.text = "$date • $time"

            ivPhoto.setOnClickListener {
                seePhoto.showPhoto(intent.getStringExtra(USER_PHOTO)!!)
            }

            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}