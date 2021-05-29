package com.example.cachingapp.features.debunks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.cachingapp.data.Debunk
import com.example.cachingapp.data.DebunkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DebunkViewModel @Inject constructor(
    private val repository: DebunkRepository
) : ViewModel() {
    val debunks = repository.getDbDebunks().asLiveData()

    fun searchDebunks(searchQuery: String): LiveData<List<Debunk>>{
        return repository.searchDebunks(searchQuery).asLiveData()
    }

}