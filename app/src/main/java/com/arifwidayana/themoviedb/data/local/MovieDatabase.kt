package com.arifwidayana.themoviedb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arifwidayana.themoviedb.data.local.dao.FavoriteDao
import com.arifwidayana.themoviedb.data.local.model.entity.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}