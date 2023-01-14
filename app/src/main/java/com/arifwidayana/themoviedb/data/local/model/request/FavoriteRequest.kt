package com.arifwidayana.themoviedb.data.local.model.request

data class FavoriteRequest(
    val idMovie: Int,
    val posterPath: String,
    val title: String,
    val overview: String,
    val rating: Double,
    val isFavorite: Boolean
)