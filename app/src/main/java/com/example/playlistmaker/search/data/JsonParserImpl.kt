package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.JsonParser
import com.google.gson.Gson

class JsonParserImpl(private val gson: Gson) : JsonParser {
    override fun <T> jsonToObject(json: String, classOfT: Class<T>): T {
        return gson.fromJson(json, classOfT)
    }
}