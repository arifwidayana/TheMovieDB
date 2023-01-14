package com.arifwidayana.themoviedb.data.local.datasource

import com.arifwidayana.themoviedb.data.local.dao.FavoriteDao
import com.arifwidayana.themoviedb.data.local.model.entity.FavoriteEntity
import com.arifwidayana.themoviedb.data.local.model.request.FavoriteRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalDatasource {
    suspend fun postInsertFavorite(favoriteRequest: FavoriteRequest)
    suspend fun getFavorite(): Flow<List<FavoriteEntity>>
    suspend fun getDetailFavorite(idMovie: Int): Flow<FavoriteEntity>
    suspend fun deleteFavorite(idMovie: Int)
}

class LocalDatasourceImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
): LocalDatasource {
    override suspend fun postInsertFavorite(favoriteRequest: FavoriteRequest) {
        return favoriteDao.postInsertMovie(
            FavoriteEntity(
                idMovie = favoriteRequest.idMovie,
                posterPath = favoriteRequest.posterPath,
                title = favoriteRequest.title,
                overview = favoriteRequest.overview,
                rating = favoriteRequest.rating,
                isFavorite = favoriteRequest.isFavorite
            )
        )
    }

    override suspend fun getFavorite(): Flow<List<FavoriteEntity>> {
        return favoriteDao.getFavorite()
    }

    override suspend fun getDetailFavorite(idMovie: Int): Flow<FavoriteEntity> {
        return favoriteDao.getDetailFavorite(idMovie)
    }

    override suspend fun deleteFavorite(idMovie: Int) {
        return favoriteDao.deleteFavorite(idMovie)
    }
}