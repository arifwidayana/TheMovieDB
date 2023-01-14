package com.arifwidayana.themoviedb.presentation.ui.info

import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.arifwidayana.themoviedb.BuildConfig
import com.arifwidayana.themoviedb.R
import com.arifwidayana.themoviedb.common.base.BaseFragment
import com.arifwidayana.themoviedb.common.wrapper.Resource
import com.arifwidayana.themoviedb.data.network.model.response.DetailMovieResponse
import com.arifwidayana.themoviedb.data.network.model.response.ReviewMovieResponse
import com.arifwidayana.themoviedb.data.network.model.response.TrailerMovieResponse
import com.arifwidayana.themoviedb.databinding.FragmentInfoBinding
import com.arifwidayana.themoviedb.presentation.adapter.info.ReviewAdapter
import com.arifwidayana.themoviedb.presentation.adapter.info.TrailerAdapter
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : BaseFragment<FragmentInfoBinding, InfoViewModel>(
    FragmentInfoBinding::inflate
) {
    private val args by navArgs<InfoFragmentArgs>()

    override fun initView() {
        onView()
        onClick()
    }

    private fun onView() {
        viewModel.apply {
            getDetailMovie(args.movieId)
            setRefreshStateFavorite()
        }
    }

    private fun onClick() {
        binding.apply {
            ivBack.setOnClickListener {
                moveNav()
            }
            btnFavorite.setOnClickListener {
                viewModel.stateFavorite(args.movieId)
            }
            btnRetryButton.setOnClickListener {
                onView()
            }
        }
    }

    private fun setRefreshStateFavorite() {
        viewModel.setFavorite(args.movieId)
    }

    override fun showLoading(isVisible: Boolean) {
        super.showLoading(isVisible)
        binding.apply {
            shimmerPlaceholder.isVisible = isVisible
            if (isVisible) {
                shimmerPlaceholder.startShimmer()
            } else {
                shimmerPlaceholder.stopShimmer()
            }
        }
    }

    override fun showContent(isVisible: Boolean) {
        super.showContent(isVisible)
        binding.nsvState.isVisible = isVisible
    }

    override fun showEmptyContent(isVisible: Boolean) {
        super.showEmptyContent(isVisible)
        binding.tvEmptyReview.isVisible = isVisible
    }

    override fun showErrorContent(isVisible: Boolean) {
        super.showErrorContent(isVisible)
        binding.clErrorView.isVisible = isVisible
    }

    override fun observeData() {
        lifecycleScope.apply {
            launchWhenStarted {
                viewModel.getDetailMovieResult.collect {
                    when (it) {
                        is Resource.Empty -> {}
                        is Resource.Loading -> {
                            showLoading(true)
                            showContent(false)
                            showErrorContent(false)
                        }
                        is Resource.Success -> {
                            showLoading(false)
                            showContent(true)
                            setStateView(it.data)
                            Log.d("TAG", "observeData: ${it.data}")
                        }
                        is Resource.Error -> {
                            showLoading(false)
                            showContent(false)
                            showErrorContent(true)
                            setErrorState(it.message)
                            Log.d("TAG", "observeData: ${it.exception} or ${it.message}")
                        }
                    }
                }
            }

            launchWhenStarted {
                viewModel.getDetailFavorite.collect {
                    if (it is Resource.Success) {
                        setStateFavorite(it.data)
                    }
                }
            }

            launchWhenStarted {
                viewModel.postInsertFavorite.collect {
                    if (it is Resource.Success) {
                        setRefreshStateFavorite()
                        showMessageSnackBar(true, it.message)
                    }
                }
            }

            launchWhenStarted {
                viewModel.deleteFavorite.collect {
                    if (it is Resource.Success) {
                        setRefreshStateFavorite()
                        showMessageSnackBar(true, it.message)
                    }
                }
            }

            launchWhenStarted {
                viewModel.getTrailerMovieResult.collect {
                    when (it) {
                        is Resource.Empty -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            it.data?.results?.let { data ->
                                setStateTrailerAdapter(data)
                            }
                            Log.d("TAG", "observeData: ${it.data}")
                        }
                        is Resource.Error -> {
                            Log.d("TAG", "observeData: ${it.exception} or ${it.message}")
                        }
                    }
                }
            }

            launchWhenStarted {
                viewModel.getReviewMovieResult.collect {
                    when (it) {
                        is Resource.Empty -> {}
                        is Resource.Loading -> {
                            showEmptyContent(false)
                        }
                        is Resource.Success -> {
                            it.data?.results?.let { data ->
                                setStateReviewAdapter(data)
                            }
                            Log.d("TAG", "observeData: ${it.data}")
                        }
                        is Resource.Error -> {
                            setStateWrong(it.message)
                            Log.d("TAG", "observeData: ${it.exception} or ${it.message}")
                        }
                    }
                }
            }
        }
    }

    private fun setStateFavorite(data: Boolean?) {
        when (data) {
            true -> setStateFav(R.drawable.ic_favorite_saved)
            else -> setStateFav(R.drawable.ic_favorite_not_saved)
        }
    }

    private fun setStateFav(isSelectorFavorite: Int) {
        binding.btnFavorite.setImageResource(isSelectorFavorite)
    }

    private fun setErrorState(data: String?) {
        binding.apply {
            tvMessageError.text = data
        }
    }

    private fun setStateWrong(data: String?) {
        binding.apply {
            showEmptyContent(true)
            tvEmptyReview.text = data
        }
    }

    private fun setStateView(data: DetailMovieResponse?) {
        binding.apply {
            data?.let { it ->
                Glide.with(root)
                    .load("${BuildConfig.BASE_IMAGE}${it.backdrop}")
                    .fitCenter()
                    .into(ivPoster)
                tvStatus.text = it.status
                tvTitle.text = it.title
                tvGenre.text = it.genres.joinToString { it?.name.toString() }
                tvRating.text = it.voteAverage.toString()
                tvReleaseDate.text = it.releaseDate
                tvOverview.text = it.overview
            }
        }
    }

    private fun setStateTrailerAdapter(data: List<TrailerMovieResponse.Result?>) {
        binding.apply {
            val adapter = TrailerAdapter(lifecycle)
            adapter.submitList(data)
            vpTrailerMovie.adapter = adapter
        }
    }

    private fun setStateReviewAdapter(data: List<ReviewMovieResponse.Result?>) {
        binding.apply {
            val adapter = ReviewAdapter()
            adapter.submitList(data)
            rvReview.adapter = adapter
        }
    }
}