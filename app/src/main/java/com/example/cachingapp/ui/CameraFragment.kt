package com.example.cachingapp.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cachingapp.R
import com.example.cachingapp.data.Debunk
import com.example.cachingapp.databinding.FragmentCameraBinding
import com.example.cachingapp.features.camera.CameraViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class CameraFragment : Fragment(R.layout.fragment_camera) {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CameraViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var debunks=emptyList<Debunk>()

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val photo = data?.extras?.get("data") as Bitmap

                runBlocking {
                    val job=launch { debunks=viewModel.textRecognition(photo) }
                    job.join()
                    val action=CameraFragmentDirections.actionCameraToDebunkList(debunks.toTypedArray(), "camera")
                    findNavController().navigate(action)}

            }
        }
    }

}