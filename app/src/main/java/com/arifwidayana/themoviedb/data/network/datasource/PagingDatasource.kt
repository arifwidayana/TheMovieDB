package com.arifwidayana.themoviedb.data.network.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arifwidayana.themoviedb.common.utils.Constant.DEFAULT_INDEX_PAGE
import com.arifwidayana.themoviedb.common.utils.Constant.NETWORK_PAGE_SIZE
import com.arifwidayana.themoviedb.data.network.model.response.MovieResponse
import com.arifwidayana.themoviedb.data.network.service.HomeService
import retrofit2.HttpException
import java.io.IOException

class PagingDatasource(
    private val homeService: HomeService
): PagingSource<Int, MovieResponse.Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse.Result> {
        return try {
            val pageIndex = params.key ?: DEFAULT_INDEX_PAGE
            val response = homeService.getTopRatedMovie(
                page = pageIndex
            )
            val nextKey =
                if (response.results.isEmpty()) {
                    null
                } else {
                    pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
                }
            LoadResult.Page(
                data = response.results,
                prevKey = if (pageIndex == DEFAULT_INDEX_PAGE) null else pageIndex - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse.Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}