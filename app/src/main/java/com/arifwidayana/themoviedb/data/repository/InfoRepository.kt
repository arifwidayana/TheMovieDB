package com.arifwidayana.themoviedb.data.repository

import com.arifwidayana.themoviedb.common.base.BaseRepository
import com.arifwidayana.themoviedb.common.wrapper.Resource
import com.arifwidayana.themoviedb.data.local.datasource.LocalDatasource
import com.arifwidayana.themoviedb.data.local.model.entity.FavoriteEntity
import com.arifwidayana.themoviedb.data.local.model.request.FavoriteRequest
import com.arifwidayana.themoviedb.data.network.datasource.InfoDatasource
import com.arifwidayana.themoviedb.data.network.model.response.DetailMovieResponse
import com.arifwidayana.themoviedb.data.network.model.response.ReviewMovieResponse
import com.arifwidayana.themoviedb.data.network.model.response.TrailerMovieResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface InfoRepository {
    suspend fun getDetailMovie(movieId: Int): Flow<Resource<DetailMovieResponse>>
    suspend fun getTrailerMovie(movieId: Int): Flow<Resource<TrailerMovieResponse>>
    suspend fun getReviewMovie(movieId: Int): Flow<Resource<ReviewMovieResponse>>
    suspend fun postInsertFavorite(favoriteRequest: FavoriteRequest): Flow<Resource<Unit>>
    suspend fun getDetailFavorite(movieId: Int): Flow<Resource<FavoriteEntity>>
    suspend fun deleteFavorite(movieId: Int): Flow<Resource<Unit>>
}

class InfoRepositoryImpl @Inject constructor(
    private val localDatasource: LocalDatasource,
    private val infoDatasource: InfoDatasource
): InfoRepository, BaseRepository() {
    override suspend fun getDetailMovie(movieId: Int): Flow<Resource<DetailMovieResponse>> = flow {
        emit(proceed { infoDatasource.getDetailMovie(movieId) })
    }

    override suspend fun getTrailerMovie(movieId: Int): Flow<Resource<TrailerMovieResponse>> = flow {
        emit(proceed { infoDatasource.getTrailerMovie(movieId) })
    }

    override suspend fun getReviewMovie(movieId: Int): Flow<Resource<ReviewMovieResponse>> = flow {
        emit(proceed { infoDatasource.getReviewMovie(movieId) })
    }

    override suspend fun postInsertFavorite(favoriteRequest: FavoriteRequest): Flow<Resource<Unit>> = flow {
        emit(proceed { localDatasource.postInsertFavorite(favoriteRequest) })
    }

    override suspend fun getDetailFavorite(movieId: Int): Flow<Resource<FavoriteEntity>> = flow {
        emit(proceed { localDatasource.getDetailFavorite(movieId).first() })
    }

    override suspend fun deleteFavorite(movieId: Int): Flow<Resource<Unit>> = flow {
        emit(proceed { localDatasource.deleteFavorite(movieId) })
    }
}