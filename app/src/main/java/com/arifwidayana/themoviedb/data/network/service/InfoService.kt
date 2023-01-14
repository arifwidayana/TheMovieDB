package com.arifwidayana.themoviedb.data.network.service

import com.arifwidayana.themoviedb.BuildConfig
import com.arifwidayana.themoviedb.common.utils.Constant.API_NAME
import com.arifwidayana.themoviedb.common.utils.Constant.ID_PATH
import com.arifwidayana.themoviedb.data.network.model.response.DetailMovieResponse
import com.arifwidayana.themoviedb.data.network.model.response.ReviewMovieResponse
import com.arifwidayana.themoviedb.data.network.model.response.TrailerMovieResponse
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface InfoService {

    @GET(BuildConfig.END_POINT_DETAIL_MOVIE)
    suspend fun getDetailMovie(@Path(ID_PATH) movieId: Int): DetailMovieResponse

    @GET(BuildConfig.END_POINT_TRAILER_MOVIE)
    suspend fun getTrailerMovie(@Path(ID_PATH) movieId: Int): TrailerMovieResponse

    @GET(BuildConfig.END_POINT_REVIEW_MOVIE)
    suspend fun getReviewMovie(@Path(ID_PATH) movieId: Int): ReviewMovieResponse

    companion object {
        @JvmStatic
        operator fun invoke(chuckerInterceptor: ChuckerInterceptor): InfoService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(chuckerInterceptor)
                .addInterceptor {
                    val req = it.request()
                    val query = req.url
                        .newBuilder()
                        .addQueryParameter(API_NAME, BuildConfig.API_KEY)
                        .build()
                    return@addInterceptor it.proceed(req.newBuilder().url(query).build())
                }
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(InfoService::class.java)
        }
    }
}