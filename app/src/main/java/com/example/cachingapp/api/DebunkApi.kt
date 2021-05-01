package com.example.cachingapp.api

import com.example.cachingapp.data.Debunk
import retrofit2.http.GET

interface DebunkApi {

    companion object{
        const val BASE_URL = "https://chrispysz.github.io/"
    }

    @GET("/Data/data.json")
    suspend fun getDebunks(): List<Debunk>
}