package com.dimasfs.storyappsubmission2.paging

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dimasfs.storyappsubmission2.databinding.StoryItemBinding
import com.dimasfs.storyappsubmission2.extensions.setLocalDateFormat
import com.dimasfs.storyappsubmission2.response.StoryItem
import com.dimasfs.storyappsubmission2.ui.detail.DetailActivity


class PagingAdapter: PagingDataAdapter<StoryItem, PagingAdapter.StoryViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }

    class StoryViewHolder(private val binding: StoryItemBinding) :

        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(ivStoryImage)
                tvStoryName.text = story.name
                tvStoryDate.setLocalDateFormat(story.createdAt)
                tvStoryDescription.text = story.description

            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_DETAIL, story)
                }
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivStoryImage, "image"),
                        Pair(binding.tvStoryName, "name"),
                        Pair(binding.tvStoryDate, "date"),
                        Pair(binding.tvStoryDescription,"description")
                    )
                it.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }


}

