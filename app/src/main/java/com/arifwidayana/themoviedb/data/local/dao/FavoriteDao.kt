package com.arifwidayana.themoviedb.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arifwidayana.themoviedb.data.local.model.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun postInsertMovie(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorite_table")
    fun getFavorite(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite_table WHERE idMovie = :idMovie")
    fun getDetailFavorite(idMovie: Int): Flow<FavoriteEntity>

    @Query("DELETE FROM favorite_table WHERE idMovie = :idMovie")
    suspend fun deleteFavorite(idMovie: Int)
}