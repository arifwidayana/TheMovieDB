package com.arifwidayana.themoviedb.presentation.adapter.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arifwidayana.themoviedb.common.utils.Constant.DEFAULT_PLAYBACK_VIDEO
import com.arifwidayana.themoviedb.data.network.model.response.TrailerMovieResponse
import com.arifwidayana.themoviedb.databinding.ItemTrailerMoviesBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class TrailerAdapter(
    private val lifecycle: Lifecycle
) : ListAdapter<TrailerMovieResponse.Result, TrailerAdapter.TrailerHolder>(
    Differ()
) {
    class TrailerHolder(
        private val binding: ItemTrailerMoviesBinding,
        private val lifecycle: Lifecycle
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentVideo: TrailerMovieResponse.Result) {
            with(binding) {
                youtubePlayerView.apply {
                    lifecycle.addObserver(youtubePlayerView)

                    addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            super.onReady(youTubePlayer)
                            currentVideo.key?.let {
                                youTubePlayer.cueVideo(
                                    videoId = it,
                                    startSeconds = DEFAULT_PLAYBACK_VIDEO
                                )
                            }
                        }
                    })
                }
            }
        }
    }

    class Differ : DiffUtil.ItemCallback<TrailerMovieResponse.Result>() {
        override fun areItemsTheSame(
            oldItem: TrailerMovieResponse.Result,
            newItem: TrailerMovieResponse.Result
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TrailerMovieResponse.Result,
            newItem: TrailerMovieResponse.Result
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerHolder {
        val binding =
            ItemTrailerMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailerHolder(binding, lifecycle)
    }

    override fun onBindViewHolder(holder: TrailerHolder, position: Int) {
        holder.bind(getItem(position))
    }
}