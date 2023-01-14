package com.arifwidayana.themoviedb.presentation.ui.favorite

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import com.arifwidayana.themoviedb.common.base.BaseFragment
import com.arifwidayana.themoviedb.common.wrapper.Resource
import com.arifwidayana.themoviedb.data.local.model.entity.FavoriteEntity
import com.arifwidayana.themoviedb.databinding.FragmentFavoriteBinding
import com.arifwidayana.themoviedb.presentation.adapter.favorite.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>(
    FragmentFavoriteBinding::inflate
) {
    override fun initView() {
        onView()
    }

    private fun onView() {
        setStateView()
        refreshSetState()
    }

    private fun setStateView() {
        viewModel.getFavorite()
    }

    private fun refreshSetState() {
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    setStateView()
                    swipeRefreshLayout.isRefreshing = false
                }, 1000)
            }
        }
    }

    override fun showContent(isVisible: Boolean) {
        super.showContent(isVisible)
        binding.swipeRefreshLayout.isVisible = isVisible
    }

    override fun showEmptyContent(isVisible: Boolean) {
        super.showEmptyContent(isVisible)
        binding.clErrorView.isVisible = isVisible
    }

    override fun observeData() {
        viewModel.getFavoriteResult.asLiveData().observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                showContent(true)
                showEmptyContent(false)
                setStateAdapter(it.data)
                Log.d("TAG", "observeData: ${it.data}")
            } else if (it is Resource.Error) {
                showContent(false)
                showEmptyContent(true)
                setEmptyState(it.message)
                Log.d("TAG", "observeData: ${it.exception} or ${it.message}")
            }
        }
    }

    private fun setStateAdapter(data: List<FavoriteEntity>?) {
        binding.apply {
            val adapter = FavoriteAdapter {
                moveNav(
                    FavoriteFragmentDirections
                        .actionFavoriteFragmentToInfoFragment()
                        .setMovieId(it)
                )
            }
            adapter.submitList(data)
            rvFavorite.adapter = adapter
        }
    }

    private fun setEmptyState(data: String?) {
        binding.apply {
            tvMessageError.text = data
        }
    }
}