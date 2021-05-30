package com.example.cachingapp.features.add

import androidx.lifecycle.ViewModel
import com.example.cachingapp.data.Debunk
import com.example.cachingapp.data.DebunkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddDebunkViewModel @Inject constructor(private val repository: DebunkRepository) : ViewModel() {

    suspend fun insertNewDebunk(d: Debunk){

    }
}