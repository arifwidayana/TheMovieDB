package com.arifwidayana.themoviedb.presentation.adapter.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arifwidayana.themoviedb.BuildConfig
import com.arifwidayana.themoviedb.data.local.model.entity.FavoriteEntity
import com.arifwidayana.themoviedb.databinding.ItemCardMoviesBinding
import com.bumptech.glide.Glide

class FavoriteAdapter(
    private val onClick: (Int) -> Unit
) : ListAdapter<FavoriteEntity, FavoriteAdapter.FavoriteHolder>(
    Differ()
) {
    class FavoriteHolder(
        private val binding: ItemCardMoviesBinding,
        private val onClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentFavorite: FavoriteEntity) {
            binding.apply {
                Glide.with(root)
                    .load("${BuildConfig.BASE_IMAGE}${currentFavorite.posterPath}")
                    .fitCenter()
                    .into(ivPoster)
                tvTitle.text = currentFavorite.title
                tvOverview.text = currentFavorite.overview
                tvRate.text = currentFavorite.rating.toString()

                root.setOnClickListener {
                    onClick(currentFavorite.idMovie)
                }
            }
        }
    }

    class Differ : DiffUtil.ItemCallback<FavoriteEntity>() {
        override fun areItemsTheSame(
            oldItem: FavoriteEntity,
            newItem: FavoriteEntity
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FavoriteEntity,
            newItem: FavoriteEntity
        ): Boolean {
            return oldItem.idMovie == newItem.idMovie
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val binding =
            ItemCardMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.bind(getItem(position))
    }
}