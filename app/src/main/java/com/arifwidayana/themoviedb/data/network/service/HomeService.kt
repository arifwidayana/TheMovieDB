package com.arifwidayana.themoviedb.data.network.service

import com.arifwidayana.themoviedb.BuildConfig
import com.arifwidayana.themoviedb.common.utils.Constant.API_NAME
import com.arifwidayana.themoviedb.common.utils.Constant.PAGE
import com.arifwidayana.themoviedb.data.network.model.response.MovieResponse
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface HomeService {

    @GET(BuildConfig.END_POINT_TOP_RATED)
    suspend fun getTopRatedMovie(@Query(PAGE) page: Int): MovieResponse

    companion object {
        @JvmStatic
        operator fun invoke(chuckerInterceptor : ChuckerInterceptor): HomeService{
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
            return retrofit.create(HomeService::class.java)
        }
    }
}