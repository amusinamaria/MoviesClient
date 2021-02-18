package com.github.amusinamaria.repository.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Long,
    val title: String,
    val overview: String,
    val poster: String,
    val backdrop: String,
    val rating: Float,
    val numberOfReviews: Int,
    val minimumAge: Int,
//    val runtime: Int,
    val genres: List<Genre>,
    val actors: List<Actor>
) : Parcelable
