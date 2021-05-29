package com.example.cachingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.cachingapp.R
import com.example.cachingapp.databinding.FragmentDebunkDetailsBinding
import com.example.cachingapp.databinding.FragmentDebunkListBinding
import kotlinx.android.synthetic.main.fragment_debunk_details.*


class DebunkDetails : Fragment(R.layout.fragment_debunk_details) {

    private var _binding: FragmentDebunkDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DebunkDetailsArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDebunkDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val debunk=args.selectedDebunk
        image_view_logo_details.setImageResource(
            when (debunk.category) {
                "covid" -> R.drawable.covid_category
                "vaccines" -> R.drawable.vaccine_category
                else -> R.drawable.no_connection
            }
        )
        text_view_sources_details.text=debunk.sources
    }
}