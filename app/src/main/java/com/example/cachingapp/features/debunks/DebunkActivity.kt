package com.example.cachingapp.features.debunks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cachingapp.databinding.ActivityDebunkBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DebunkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDebunkBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}