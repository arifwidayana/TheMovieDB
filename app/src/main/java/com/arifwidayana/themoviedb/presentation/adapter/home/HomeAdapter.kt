package com.arifwidayana.themoviedb.presentation.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arifwidayana.themoviedb.BuildConfig
import com.arifwidayana.themoviedb.common.mapper.model.ParamMovieMapper
import com.arifwidayana.themoviedb.databinding.ItemCardMoviesBinding
import com.bumptech.glide.Glide

class HomeAdapter(
    private val onClick: (Int) -> Unit,
) : PagingDataAdapter<ParamMovieMapper, HomeAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    inner class MovieViewHolder(
        private val binding: ItemCardMoviesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ParamMovieMapper) {
            with(binding) {
                Glide.with(root)
                    .load("${BuildConfig.BASE_IMAGE}${data.image}")
                    .fitCenter()
                    .into(ivPoster)
                tvTitle.text = data.title
                tvOverview.text = data.overview
                tvRate.text = data.voteAverage.toString()

                root.setOnClickListener {
                    onClick.invoke(data.id)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemCardMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ParamMovieMapper>() {
            override fun areItemsTheSame(oldItem: ParamMovieMapper, newItem: ParamMovieMapper): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ParamMovieMapper, newItem: ParamMovieMapper): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}