package com.arifwidayana.themoviedb.presentation.adapter.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arifwidayana.themoviedb.BuildConfig
import com.arifwidayana.themoviedb.R
import com.arifwidayana.themoviedb.data.network.model.response.ReviewMovieResponse
import com.arifwidayana.themoviedb.databinding.ItemReviewMoviesBinding
import com.bumptech.glide.Glide

class ReviewAdapter: ListAdapter<ReviewMovieResponse.Result, ReviewAdapter.ReviewHolder>(
    Differ()
) {
    class ReviewHolder(
        private val binding: ItemReviewMoviesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentReview: ReviewMovieResponse.Result) {
            binding.apply {
                Glide.with(root)
                    .load("${BuildConfig.BASE_IMAGE}${currentReview.authorDetails?.avatarPath}")
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(sivAuthor)
                tvAuthor.text = currentReview.author
                currentReview.authorDetails?.rating?.let {
                    ratingBar.rating = it.toFloat()
                }
                tvLastUpdate.text = currentReview.updatedAt
                tvContentReview.text = currentReview.content
            }
        }
    }

    class Differ : DiffUtil.ItemCallback<ReviewMovieResponse.Result>() {
        override fun areItemsTheSame(
            oldItem: ReviewMovieResponse.Result,
            newItem: ReviewMovieResponse.Result
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ReviewMovieResponse.Result,
            newItem: ReviewMovieResponse.Result
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val binding =
            ItemReviewMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}