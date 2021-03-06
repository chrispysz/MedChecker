package com.example.cachingapp.ui

import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cachingapp.R
import com.example.cachingapp.data.DebunkRepository
import com.example.cachingapp.databinding.FragmentDebunkListBinding
import com.example.cachingapp.features.debunks.DebunkAdapter
import com.example.cachingapp.features.debunks.DebunkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class DebunkList : Fragment(R.layout.fragment_debunk_list) {
    private var _binding: FragmentDebunkListBinding? = null
    private val binding get() = _binding!!
    private lateinit var debunkAdapter: DebunkAdapter
    lateinit var searchView: SearchView

    @Inject
    lateinit var repository: DebunkRepository

    val args: DebunkListArgs by navArgs()

    private val viewModel: DebunkViewModel by viewModels()

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottomUp: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.from_bottom_up
        )
    }
    private val toBottomDown: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.to_bottom_down
        )
    }
    private var clicked = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDebunkListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        debunkAdapter = DebunkAdapter()

        binding.expandFab.setOnClickListener {
            onExpandButtonClicked(binding)
        }

        binding.photoFab.setOnClickListener {
            onCameraButtonClicked(binding)
        }

        binding.clipboardFab.setOnClickListener {
            onClipboardButtonClicked(binding)
        }

        binding.syncFab.setOnClickListener {
            onSyncButtonClicked()
        }

        binding.addFab.setOnClickListener {
            onNewDebunkButtonClicked(binding)
        }


        binding.apply {
            recyclerView.apply {
                adapter = debunkAdapter
                layoutManager = LinearLayoutManager(context)
            }

            viewModel.debunks.observe(viewLifecycleOwner) { result ->
                debunkAdapter.submitList(result)
                result.let { debunkAdapter.setData(it) }

            }
        }

    }

    private fun onSyncButtonClicked() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                repository.getApiDebunks()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Debunks have been successfully updated!", Toast.LENGTH_LONG)
                        .show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error occurred while syncing data.\nCheck your internet connection!", Toast.LENGTH_LONG)
                        .show()
                }

            }
        }

    }

    private fun onNewDebunkButtonClicked(binding: FragmentDebunkListBinding) {
        val action = DebunkListDirections.actionDebunkListToFavourites()
        binding.addFab.findNavController().navigate(action)


    }

    private fun onClipboardButtonClicked(binding: FragmentDebunkListBinding) {
        val action = DebunkListDirections.actionDebunkListToClipboardFragment()
        binding.clipboardFab.findNavController().navigate(action)


    }

    private fun onCameraButtonClicked(binding: FragmentDebunkListBinding) {
        val action = DebunkListDirections.actionDebunkListToCamera()
        binding.photoFab.findNavController().navigate(action)
    }


    private fun onExpandButtonClicked(binding: FragmentDebunkListBinding) {
        setVisibility(clicked, binding)
        setAnimation(clicked, binding)
        setClickable(clicked, binding)
        clicked = !clicked
    }

    private fun setClickable(clicked: Boolean, binding: FragmentDebunkListBinding) {
        if (!clicked) {
            binding.clipboardFab.isClickable = true
            binding.photoFab.isClickable = true
            binding.syncFab.isClickable = true
            binding.addFab.isClickable = true
        } else {
            binding.clipboardFab.isClickable = false
            binding.photoFab.isClickable = false
            binding.syncFab.isClickable = false
            binding.addFab.isClickable = false
        }
    }

    private fun setAnimation(clicked: Boolean, binding: FragmentDebunkListBinding) {
        if (!clicked) {
            binding.clipboardFab.startAnimation(fromBottomUp)
            binding.photoFab.startAnimation(fromBottomUp)
            binding.syncFab.startAnimation(fromBottomUp)
            binding.addFab.startAnimation(fromBottomUp)
            binding.expandFab.startAnimation(rotateOpen)
        } else {
            binding.clipboardFab.startAnimation(toBottomDown)
            binding.photoFab.startAnimation(toBottomDown)
            binding.syncFab.startAnimation(toBottomDown)
            binding.addFab.startAnimation(toBottomDown)
            binding.expandFab.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean, binding: FragmentDebunkListBinding) {
        if (!clicked) {
            binding.clipboardFab.visibility = View.VISIBLE
            binding.photoFab.visibility = View.VISIBLE
            binding.syncFab.visibility = View.VISIBLE
            binding.addFab.visibility = View.VISIBLE
        } else {
            binding.clipboardFab.visibility = View.INVISIBLE
            binding.photoFab.visibility = View.INVISIBLE
            binding.syncFab.visibility = View.INVISIBLE
            binding.addFab.visibility = View.INVISIBLE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val item = menu.findItem(R.id.action_search)
        searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchDebunks(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    searchDebunks(query)
                }
                return true
            }

        })
    }

    private fun searchDebunks(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchDebunks(searchQuery).observe(viewLifecycleOwner, { list ->
            list.let {
                debunkAdapter.submitList(it)
                debunkAdapter.setData(it)
            }
        })
    }

    private fun setQueryText(text: String) {
        if (args.from == "clipboard" || args.from == "camera")
            searchView.setQuery(text, true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        args.filterCategory?.let { setQueryText(it) }
        super.onPrepareOptionsMenu(menu)
    }


}