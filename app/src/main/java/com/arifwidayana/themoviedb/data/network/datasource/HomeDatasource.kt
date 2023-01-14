package com.arifwidayana.themoviedb.data.network.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arifwidayana.themoviedb.common.utils.Constant.NETWORK_PAGE_SIZE
import com.arifwidayana.themoviedb.data.network.model.response.MovieResponse
import com.arifwidayana.themoviedb.data.network.service.HomeService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface HomeDatasource {
    suspend fun getMovies(): Flow<PagingData<MovieResponse.Result>>
}

class HomeDatasourceImpl @Inject constructor(
    private val homeService: HomeService
): HomeDatasource {

    override suspend fun getMovies(): Flow<PagingData<MovieResponse.Result>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingDatasource(homeService = homeService)
            }
        ).flow
    }
}
