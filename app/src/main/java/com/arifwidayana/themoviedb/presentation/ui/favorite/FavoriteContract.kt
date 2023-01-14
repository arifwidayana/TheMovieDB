package com.arifwidayana.themoviedb.presentation.ui.favorite

import com.arifwidayana.themoviedb.common.wrapper.Resource
import com.arifwidayana.themoviedb.data.local.model.entity.FavoriteEntity
import kotlinx.coroutines.flow.StateFlow

interface FavoriteContract {
    val getFavoriteResult: StateFlow<Resource<List<FavoriteEntity>>>
    fun getFavorite()
}