package com.example.cachingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cachingapp.R
import com.example.cachingapp.data.Debunk
import com.example.cachingapp.data.DebunkRepository
import com.example.cachingapp.databinding.FragmentCameraBinding
import com.example.cachingapp.databinding.FragmentFavouritesBinding
import com.example.cachingapp.features.add.AddDebunkViewModel
import com.example.cachingapp.features.camera.CameraViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.debunk_item.*
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var repository: DebunkRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.add_spinner,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                binding.spinnerCategory.adapter = adapter
            }
        }

        binding.buttonSubmit.setOnClickListener {

            val question=binding.editTextQuestion.text.toString()
            val answer=binding.editTextAnswer.text.toString()
            val category=binding.spinnerCategory.selectedItem.toString()
            val sources = binding.editTextSources.text.toString()
            val state = "offline"
            val d=Debunk(0, category, question, answer, sources, state)
            onSubmitClick(d)

        }

    }

    private fun onSubmitClick(d: Debunk) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.insertDebunk(d)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Debunk successfully added!", Toast.LENGTH_LONG)
                    .show()
                val action = FavouritesFragmentDirections.actionFavouritesToDebunkList()
                findNavController().navigate(action)
            }

        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }


}