package com.example.cachingapp.features.clipboard

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cachingapp.data.Debunk
import com.example.cachingapp.data.DebunkRepository
import com.example.cachingapp.util.Resource
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.lang.StringBuilder
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ClipboardViewModel @Inject constructor(private val repository: DebunkRepository) : ViewModel() {

    private lateinit var text: String


    fun analyzeFromClipboard(text: String): String {

        var filter=""

        this.text=text
        if (this.text == "") {
            Log.i("clip","Clipboard is empty! Copy something first")
        } else {

            filter=fetchBiggestCategoryData(getOrderedOccurrences())

            val builder = StringBuilder()
            for (i in 1 until getOrderedOccurrences().size) {
                builder.append(getOrderedOccurrences()[i]).append(" ")
            }
            Log.i("clip","Your text is mostly about: ${getOrderedOccurrences()[0]}\n" +
                    "Other categories found: $builder")
        }

        return filter
    }

    private  fun fetchBiggestCategoryData(l: ArrayList<String>): String {
        return l[0]
    }

    private fun getOrderedOccurrences(): ArrayList<String> {
        val covidCat = arrayListOf("covid", "sars-cov", "koronawirus", "korona")
        var covidCnt = 0
        val vaccineCat = arrayListOf("vaccine", "vax", "szczepienia", "szczepionki", "szczepień")
        var vaccineCnt = 0

        for (item in covidCat) {
            covidCnt += countOccurrences(item)
        }
        for (item in vaccineCat) {
            vaccineCnt += countOccurrences(item)
        }

        val list =
            mutableListOf(covidCnt to "covid", vaccineCnt to "vaccines").takeWhile { it.first > 0 }
        val listSorted = list.sortedByDescending { it.first }
        val orderedCategoriesList = ArrayList<String>()

        for (item in listSorted) {
            orderedCategoriesList.add(item.second)
        }
        if (orderedCategoriesList.isEmpty()) orderedCategoriesList.add("none")
        return orderedCategoriesList
    }

    private fun countOccurrences(category: String): Int {
        text = text.toLowerCase(Locale.ROOT)
        return text.windowed(category.length).count { it == category }
    }

}