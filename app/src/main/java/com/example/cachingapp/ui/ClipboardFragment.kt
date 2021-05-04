package com.example.cachingapp.ui

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cachingapp.R
import com.example.cachingapp.data.Debunk
import com.example.cachingapp.databinding.FragmentCameraBinding
import com.example.cachingapp.features.camera.CameraViewModel
import com.example.cachingapp.features.clipboard.ClipboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class ClipboardFragment : Fragment(R.layout.fragment_clipboard) {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClipboardViewModel by viewModels()

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
        val clipboard =
            context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val item = clipboard.primaryClip?.getItemAt(0)
        val text = item?.text.toString()
        val filter = viewModel.analyzeFromClipboard(text)
        val action = ClipboardFragmentDirections.actionClipboardFragmentToDebunkList(
            filter,
            "clipboard"
        )
        findNavController().navigate(action)
    }

}
