package com.example.cachingapp.features.debunks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.cachingapp.data.DebunkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DebunkViewModel @Inject constructor(
    repository: DebunkRepository
) : ViewModel() {
    val debunks = repository.getDebunks().asLiveData()


}