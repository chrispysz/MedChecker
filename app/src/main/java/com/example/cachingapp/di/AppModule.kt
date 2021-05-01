package com.example.cachingapp.di

import android.app.Application
import androidx.room.Room
import com.example.cachingapp.api.DebunkApi
import com.example.cachingapp.data.DebunkDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder().baseUrl(DebunkApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun provideDebunkApi(retrofit: Retrofit): DebunkApi =
        retrofit.create(DebunkApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): DebunkDatabase =
        Room.databaseBuilder(app, DebunkDatabase::class.java, "debunk_database").build()
}