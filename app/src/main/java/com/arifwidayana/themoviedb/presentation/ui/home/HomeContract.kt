package com.arifwidayana.themoviedb.presentation.ui.home

import androidx.paging.PagingData
import com.arifwidayana.themoviedb.common.mapper.model.ParamMovieMapper
import com.arifwidayana.themoviedb.common.wrapper.Resource
import kotlinx.coroutines.flow.StateFlow

interface HomeContract {
    val getMovieResult: StateFlow<Resource<PagingData<ParamMovieMapper>>>
    fun getMovie()
}