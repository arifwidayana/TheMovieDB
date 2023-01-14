package com.arifwidayana.themoviedb.presentation.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arifwidayana.themoviedb.common.wrapper.Resource
import com.arifwidayana.themoviedb.data.local.model.request.FavoriteRequest
import com.arifwidayana.themoviedb.data.network.model.response.DetailMovieResponse
import com.arifwidayana.themoviedb.data.network.model.response.ReviewMovieResponse
import com.arifwidayana.themoviedb.data.network.model.response.TrailerMovieResponse
import com.arifwidayana.themoviedb.data.repository.InfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val infoRepository: InfoRepository
): InfoContract, ViewModel() {
    private val _getDetailMovieResult = MutableStateFlow<Resource<DetailMovieResponse>>(Resource.Empty())
    private val _getTrailerMovieResult = MutableStateFlow<Resource<TrailerMovieResponse>>(Resource.Empty())
    private val _getReviewMovieResult = MutableStateFlow<Resource<ReviewMovieResponse>>(Resource.Empty())
    private val _getDetailFavorite = MutableStateFlow<Resource<Boolean>>(Resource.Empty())
    private val _postInsertFavorite = MutableStateFlow<Resource<Unit>>(Resource.Empty())
    private val _deleteFavorite = MutableStateFlow<Resource<Unit>>(Resource.Empty())
    override val getDetailMovieResult: StateFlow<Resource<DetailMovieResponse>> = _getDetailMovieResult
    override val getTrailerMovieResult: StateFlow<Resource<TrailerMovieResponse>> =  _getTrailerMovieResult
    override val getReviewMovieResult: StateFlow<Resource<ReviewMovieResponse>> = _getReviewMovieResult
    override val getDetailFavorite: StateFlow<Resource<Boolean>> = _getDetailFavorite
    override val postInsertFavorite: StateFlow<Resource<Unit>> = _postInsertFavorite
    override val deleteFavorite: StateFlow<Resource<Unit>> = _deleteFavorite

    override fun getDetailMovie(movieId: Int) {
        viewModelScope.launch {
            _getDetailMovieResult.value = Resource.Loading()
            infoRepository.getDetailMovie(movieId).collect {
                try {
                    if (it.data != null) {
                        _getDetailMovieResult.value = Resource.Success(data = it.data)
                    } else {
                        _getDetailMovieResult.value = Resource.Error(message = "Something went wrong, check your connection!")
                    }
                } catch (e: Exception) {
                    _getDetailMovieResult.value = Resource.Error(message = e.message.orEmpty())
                }
            }
        }

        viewModelScope.launch {
            _getTrailerMovieResult.value = Resource.Loading()
            infoRepository.getTrailerMovie(movieId).collect {
                try {
                    if (!it.data?.results.isNullOrEmpty()) {
                        _getTrailerMovieResult.value = Resource.Success(data = it.data)
                    } else {
                        _getTrailerMovieResult.value = Resource.Error(message = "Data is empty")
                    }
                } catch (e: Exception) {
                    _getTrailerMovieResult.value = Resource.Error(message = e.message.orEmpty())
                }
            }
        }

        viewModelScope.launch {
            _getReviewMovieResult.value = Resource.Loading()
            infoRepository.getReviewMovie(movieId).collect {
                try {
                    if (!it.data?.results.isNullOrEmpty()) {
                        _getReviewMovieResult.value = Resource.Success(data = it.data)
                    } else {
                        _getReviewMovieResult.value = Resource.Error(message = "This movie has no reviews")
                    }
                } catch (e: Exception) {
                    _getReviewMovieResult.value = Resource.Error(message = e.message.orEmpty())
                }
            }
        }
    }

    override fun setFavorite(movieId: Int) {
        viewModelScope.launch {
            infoRepository.getDetailFavorite(movieId).collect {
                _getDetailFavorite.value = Resource.Success(it.data?.isFavorite)
            }
        }
    }

    override fun stateFavorite(movieId: Int) {
        viewModelScope.launch {
            infoRepository.getDetailFavorite(movieId).collect {
                if (it.data?.idMovie == null) {
                    infoRepository.getDetailMovie(movieId).collect { source ->
                        source.data?.let { res ->
                            res.voteAverage?.let { vote ->
                                infoRepository.postInsertFavorite(
                                    FavoriteRequest(
                                        idMovie = movieId,
                                        posterPath = res.posterPath.toString(),
                                        title = res.title.toString(),
                                        overview = res.overview.toString(),
                                        rating = vote,
                                        isFavorite = true
                                    )
                                ).collect {
                                    _postInsertFavorite.value = Resource.Success(message = "Movie saved to collection")
                                }
                            }
                        }
                    }
                } else {
                    infoRepository.deleteFavorite(movieId).collect {
                        _deleteFavorite.value = Resource.Success(message = "Movie has deleted from collection")
                    }
                }
            }
        }
    }
}