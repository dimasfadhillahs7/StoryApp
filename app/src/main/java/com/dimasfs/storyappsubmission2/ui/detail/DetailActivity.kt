package com.dimasfs.storyappsubmission2.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dimasfs.storyappsubmission2.databinding.ActivityDetailBinding
import com.dimasfs.storyappsubmission2.extensions.setLocalDateFormat
import com.dimasfs.storyappsubmission2.response.StoryItem

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val dataStory = intent.getParcelableExtra<StoryItem>(EXTRA_DETAIL)

        binding.apply {
            Glide.with(this@DetailActivity)
                .load(dataStory?.photoUrl)
                .apply(RequestOptions().override(1000))
                .into(ivStoryImage)
            tvStoryUsername.text = dataStory?.name
            tvStoryDate.setLocalDateFormat(dataStory!!.createdAt)
            tvStoryDescription.text = dataStory?.description
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}