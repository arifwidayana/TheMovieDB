package com.arifwidayana.themoviedb.presentation.ui.info

import com.arifwidayana.themoviedb.common.wrapper.Resource
import com.arifwidayana.themoviedb.data.network.model.response.DetailMovieResponse
import com.arifwidayana.themoviedb.data.network.model.response.ReviewMovieResponse
import com.arifwidayana.themoviedb.data.network.model.response.TrailerMovieResponse
import kotlinx.coroutines.flow.StateFlow

interface InfoContract {
    val getDetailMovieResult: StateFlow<Resource<DetailMovieResponse>>
    val getTrailerMovieResult: StateFlow<Resource<TrailerMovieResponse>>
    val getReviewMovieResult: StateFlow<Resource<ReviewMovieResponse>>
    val getDetailFavorite: StateFlow<Resource<Boolean>>
    val postInsertFavorite: StateFlow<Resource<Unit>>
    val deleteFavorite: StateFlow<Resource<Unit>>
    fun getDetailMovie(movieId: Int)
    fun setFavorite(movieId: Int)
    fun stateFavorite(movieId: Int)
}