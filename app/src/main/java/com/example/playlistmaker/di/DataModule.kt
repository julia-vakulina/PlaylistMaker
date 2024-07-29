package com.example.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.search.data.ItunesAPI
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.RetrofitNetworkClient
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.settings.data.ExternalNavigator
import com.example.playlistmaker.settings.data.NavigatorRepositoryImpl
import com.example.playlistmaker.settings.domain.NavigatorRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {
    single<ItunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ItunesAPI::class.java)
    }
    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }
    single<TrackRepository> {
        TrackRepositoryImpl(get(), get(), get())
    }
    single {
        androidContext()
            .getSharedPreferences("history", Context.MODE_PRIVATE)
    }
    factory { Gson() }
    single<SearchHistory> { get() }
    single<NavigatorRepository> {
        NavigatorRepositoryImpl(get(), androidContext())
    }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
}


