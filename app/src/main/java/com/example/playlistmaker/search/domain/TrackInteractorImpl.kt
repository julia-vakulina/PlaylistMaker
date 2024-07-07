package com.example.playlistmaker.search.domain

import com.example.playlistmaker.player.domain.TrackFromAPI
import com.example.playlistmaker.search.data.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TrackInteractorImpl (private val repository: TrackRepository) : TrackInteractor {
    override fun searchTrack(expression: String): Flow<Pair<List<TrackFromAPI>?, String?>> {
        //executor.execute {
        //    when (val resource = repository.searchTrack(expression)) {
        //        is Resource.Success -> {
        //            consumer.consume(resource.data, null)
        //        }
        //        is Resource.Error -> {
        //            consumer.consume(null, resource.message)
        //        }
        //    }
        //}
        return repository.searchTrack(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

}