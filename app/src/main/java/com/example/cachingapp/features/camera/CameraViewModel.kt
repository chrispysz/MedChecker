package com.example.cachingapp.features.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cachingapp.data.DebunkRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CameraViewModel @Inject constructor(private val repository: DebunkRepository) : ViewModel() {

    private lateinit var text: String


    fun textRecognition(photo: Bitmap): String {

        val image = InputImage.fromBitmap(photo, 0)

        val recognizer = TextRecognition.getClient()
        var filter= ""

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Task completed successfully
                text = visionText.text
                filter = fetchBiggestCategoryData(getOrderedOccurrences())


                val builder = StringBuilder()
                for (i in 1 until getOrderedOccurrences().size) {
                    builder.append(getOrderedOccurrences()[i]).append(" ")
                }
                Log.i(
                    "clip", "Your text is mostly about: ${getOrderedOccurrences()[0]}\n" +
                            "Other categories found: $builder"
                )

            }
            .addOnFailureListener {
                // Task failed with an exception
                Log.i("clip", "Task failed")
            }

        return filter
    }

    private fun fetchBiggestCategoryData(l: ArrayList<String>): String {
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