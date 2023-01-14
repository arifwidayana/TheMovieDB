package com.arifwidayana.themoviedb.presentation.ui.home

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.arifwidayana.themoviedb.R
import com.arifwidayana.themoviedb.common.base.BaseFragment
import com.arifwidayana.themoviedb.common.mapper.model.ParamMovieMapper
import com.arifwidayana.themoviedb.common.wrapper.Resource
import com.arifwidayana.themoviedb.databinding.FragmentHomeBinding
import com.arifwidayana.themoviedb.presentation.adapter.home.HomeAdapter
import com.arifwidayana.themoviedb.presentation.adapter.home.LoadingStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate
) {
    private lateinit var adapter: HomeAdapter

    override fun initView() {
        onView()
        onClick()
    }

    private fun onView() {
        binding.apply {
            viewModel.getMovie()
            swipeRefreshLayout.setOnRefreshListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.getMovie()
                    swipeRefreshLayout.isRefreshing = false
                }, 1000)
            }
        }
    }

    private fun onClick() {
        binding.apply {
            btnRetryButton.setOnClickListener {
                viewModel.getMovie()
            }
        }
    }

    override fun showLoading(isVisible: Boolean) {
        super.showEmptyContent(isVisible)
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
        binding.swipeRefreshLayout.isVisible = isVisible
    }

    override fun showErrorContent(isVisible: Boolean) {
        super.showErrorContent(isVisible)
        binding.clErrorView.isVisible = isVisible
    }

    override fun observeData() {
        viewModel.getMovieResult.asLiveData().observe(viewLifecycleOwner) {
            if(it is Resource.Success) {
                it.data?.let { res ->
                    setStateMovie(res)
                }
                Log.d("TAG", "observeData: ${it.data?.toString()}")
            } else if (it is Resource.Error) {
                Log.d("TAG", "observeData: ${it.exception}")
            }
        }
    }

    private fun setStateMovie(data: PagingData<ParamMovieMapper>) {
        binding.apply {
            adapter = HomeAdapter {
                val parcel = HomeFragmentDirections.actionHomeFragmentToInfoFragment()
                parcel.movieId = it
                moveNav(parcel)
            }
            adapter.loadStateFlow.asLiveData().observe(viewLifecycleOwner) {
                when(it.source.refresh) {
                    is LoadState.Loading -> {
                        showLoading(true)
                        showContent(false)
                        showErrorContent(false)
                    }
                    is LoadState.Error -> {
                        showLoading(false)
                        showContent(false)
                        setErrorState()
                    }
                    else -> {
                        showLoading(false)
                        showContent(true)
                        showErrorContent(false)
                    }
                }
            }
            adapter.submitData(lifecycle, data)
            rvMovie.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
        }
    }

    private fun setErrorState() {
        binding.apply {
            showErrorContent(true)
            tvMessageError.text = getString(R.string.network_error)
        }
    }
}