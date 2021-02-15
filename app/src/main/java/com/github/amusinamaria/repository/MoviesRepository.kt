package com.github.amusinamaria.repository

import android.util.Log
import com.github.amusinamaria.R
import com.github.amusinamaria.network.MovieFromNetwork
import com.github.amusinamaria.network.NetworkModule
import com.github.amusinamaria.repository.data.Genre
import com.github.amusinamaria.repository.data.Movie
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MoviesRepository @Inject constructor() {

//    private val jsonFormat = Json { ignoreUnknownKeys = true }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            "MoviesRepository",
            "Coroutine exception",
            throwable
        )
        val errorTextId = when (throwable) {
            is IOException, is HttpException -> R.string.internet_connection_error
            is SerializationException -> R.string.parsing_error
            else -> R.string.unexpected_error
        }
        Log.e("MoviesRepository", errorTextId.toString())
    }

    @ExperimentalSerializationApi
    suspend fun loadMoviesFromNetwork(): List<Movie> {
        var moviesFromNetwork = listOf<MovieFromNetwork>()
        var genres = listOf<Genre>()
        coroutineScope {
            launch(exceptionHandler) {
                moviesFromNetwork = NetworkModule.moviesApi.getNowPlayingMovies().results
            }
            launch(exceptionHandler) {
                genres = NetworkModule.moviesApi.getGenres().genres
            }
        }
        return createMoviesObjects(moviesFromNetwork, genres)
    }

    private fun createMoviesObjects(
        moviesFromNetwork: List<MovieFromNetwork>,
        genres: List<Genre>,
    ): List<Movie> {
        val genresMap = genres.associateBy { genre -> genre.id }
        return moviesFromNetwork.map { movieFromNetwork ->
            Movie(
                id = movieFromNetwork.id,
                title = movieFromNetwork.title,
                poster = movieFromNetwork.poster.orEmpty(),
                backdrop = movieFromNetwork.backdrop.orEmpty(),
                rating = movieFromNetwork.rating,
                minimumAge = if (movieFromNetwork.adult) 16 else 13,
                numberOfReviews = movieFromNetwork.numberOfReviews,
                overview = movieFromNetwork.overview,
                genres = movieFromNetwork.genreIds.map { id ->
                    genresMap[id] ?: throw IllegalArgumentException("Genre not found")
                }
            )
        }
    }
}