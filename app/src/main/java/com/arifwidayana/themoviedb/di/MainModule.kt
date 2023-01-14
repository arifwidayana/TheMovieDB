package com.arifwidayana.themoviedb.di

import android.content.Context
import androidx.room.Room
import com.arifwidayana.themoviedb.common.base.BaseGenericViewModel
import com.arifwidayana.themoviedb.common.mapper.MovieMapper
import com.arifwidayana.themoviedb.common.mapper.MovieMapperImpl
import com.arifwidayana.themoviedb.common.utils.Constant
import com.arifwidayana.themoviedb.data.local.MovieDatabase
import com.arifwidayana.themoviedb.data.local.dao.FavoriteDao
import com.arifwidayana.themoviedb.data.local.datasource.LocalDatasource
import com.arifwidayana.themoviedb.data.local.datasource.LocalDatasourceImpl
import com.arifwidayana.themoviedb.data.network.datasource.HomeDatasource
import com.arifwidayana.themoviedb.data.network.datasource.HomeDatasourceImpl
import com.arifwidayana.themoviedb.data.network.datasource.InfoDatasource
import com.arifwidayana.themoviedb.data.network.datasource.InfoDatasourceImpl
import com.arifwidayana.themoviedb.data.network.service.HomeService
import com.arifwidayana.themoviedb.data.network.service.InfoService
import com.arifwidayana.themoviedb.data.repository.*
import com.arifwidayana.themoviedb.presentation.ui.favorite.FavoriteViewModel
import com.arifwidayana.themoviedb.presentation.ui.home.HomeViewModel
import com.arifwidayana.themoviedb.presentation.ui.info.InfoViewModel
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object MainModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object LocalModule {
        @Provides
        @Singleton
        fun provideMovieDatabase(
            @ApplicationContext context: Context
        ): MovieDatabase = Room
            .databaseBuilder(
                context,
                MovieDatabase::class.java,
                Constant.DB_NAME
            )
            .build()

        @Provides
        @Singleton
        fun provideFavoriteDao(movieDatabase: MovieDatabase) = movieDatabase.favoriteDao()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule {
        @Provides
        @Singleton
        fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
            return ChuckerInterceptor.Builder(context).build()
        }

        @Provides
        @Singleton
        fun provideHomeService(chuckerInterceptor: ChuckerInterceptor): HomeService {
            return HomeService.invoke(chuckerInterceptor)
        }

        @Provides
        @Singleton
        fun provideInfoService(chuckerInterceptor: ChuckerInterceptor): InfoService {
            return InfoService.invoke(chuckerInterceptor)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object DataSourceModule {
        @Provides
        @Singleton
        fun provideMovieMapper(): MovieMapper {
            return MovieMapperImpl()
        }

        @Provides
        @Singleton
        fun provideLocalDatasource(favoriteDao: FavoriteDao): LocalDatasource {
            return LocalDatasourceImpl(favoriteDao)
        }

        @Provides
        @Singleton
        fun provideMovieDatasource(homeService: HomeService): HomeDatasource {
            return HomeDatasourceImpl(homeService)
        }

        @Provides
        @Singleton
        fun provideInfoDatasource(infoService: InfoService): InfoDatasource {
            return InfoDatasourceImpl(infoService)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object RepositoryModule {
        @Provides
        @Singleton
        fun provideHomeRepository(homeDatasource: HomeDatasource, movieMapper: MovieMapper): HomeRepository {
            return HomeRepositoryImpl(homeDatasource, movieMapper)
        }

        @Provides
        @Singleton
        fun provideInfoRepository(localDatasource: LocalDatasource, infoDatasource: InfoDatasource): InfoRepository {
            return InfoRepositoryImpl(localDatasource, infoDatasource)
        }

        @Provides
        @Singleton
        fun provideFavoriteRepository(localDatasource: LocalDatasource): FavoriteRepository {
            return FavoriteRepositoryImpl(localDatasource)
        }
    }

    @Module
    @InstallIn(FragmentComponent::class)
    object ViewModelModule {
        @Provides
        @FragmentScoped
        fun provideHomeViewModel(homeRepository: HomeRepository): HomeViewModel {
            return BaseGenericViewModel(HomeViewModel(homeRepository)).create(
                HomeViewModel::class.java
            )
        }

        @Provides
        @FragmentScoped
        fun provideInfoViewModel(infoRepository: InfoRepository): InfoViewModel {
            return BaseGenericViewModel(InfoViewModel(infoRepository)).create(
                InfoViewModel::class.java
            )
        }

        @Provides
        @FragmentScoped
        fun provideFavoriteViewModel(favoriteRepository: FavoriteRepository): FavoriteViewModel {
            return BaseGenericViewModel(FavoriteViewModel(favoriteRepository)).create(
                FavoriteViewModel::class.java
            )
        }
    }
}