package com.example.cachingapp.features.debunks

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.cachingapp.data.DebunkRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.StringBuilder
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class DebunkViewModel @Inject constructor(
    repository: DebunkRepository
) : ViewModel() {
    val debunks = repository.getDebunks().asLiveData()


}