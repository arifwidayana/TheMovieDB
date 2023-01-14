package com.arifwidayana.themoviedb.common.mapper

import com.arifwidayana.themoviedb.common.mapper.model.ParamMovieMapper
import com.arifwidayana.themoviedb.data.network.model.response.MovieResponse
import javax.inject.Inject

interface MovieMapper {
    fun mapRemoteMoviesListToData(remoteMovies: List<MovieResponse.Result>): List<ParamMovieMapper>
    fun mapRemoteMovieToData(remoteMovie: MovieResponse.Result): ParamMovieMapper
}

class MovieMapperImpl @Inject constructor() : MovieMapper {
    override fun mapRemoteMoviesListToData(remoteMovies: List<MovieResponse.Result>): List<ParamMovieMapper> {
        return remoteMovies.map {
            mapRemoteMovieToData(it)
        }
    }

    override fun mapRemoteMovieToData(remoteMovie: MovieResponse.Result): ParamMovieMapper {
        return ParamMovieMapper(
            id = remoteMovie.id ?: 0,
            isAdultOnly = remoteMovie.adult ?: false,
            popularity = remoteMovie.popularity ?: 0.0,
            voteAverage = remoteMovie.voteAverage ?: 0.0,
            voteCount = remoteMovie.voteCount ?: 0,
            image = remoteMovie.posterPath ?: remoteMovie.backdropPath ?: "",
            title = remoteMovie.title ?:remoteMovie.originalTitle ?: "No title found",
            overview = remoteMovie.overview ?: "No overview found",
            releaseDate = remoteMovie.releaseDate ?: "No date found",
            originalLanguage = remoteMovie.originalLanguage ?: "No language found"
        )
    }
}