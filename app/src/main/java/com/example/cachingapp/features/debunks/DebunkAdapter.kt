package com.example.cachingapp.features.debunks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cachingapp.R
import com.example.cachingapp.data.Debunk
import com.example.cachingapp.databinding.DebunkItemBinding
import com.example.cachingapp.ui.DebunkListDirections

class DebunkAdapter : ListAdapter<Debunk, DebunkAdapter.DebunkViewHolder>(DebunkComparator()) {

    var debunkList= emptyList<Debunk>()

    inner class DebunkViewHolder(private val binding: DebunkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageViewLogo.setOnClickListener {
                val position=adapterPosition
                val action=DebunkListDirections.actionDebunkListToDebunkDetails(debunkList[position])
                binding.imageViewLogo.findNavController().navigate(action)
            }
        }

        fun bind(debunk: Debunk) {
            binding.apply {
                imageViewLogo.setImageResource(
                    when (debunk.category) {
                        "covid" -> R.drawable.covid_category
                        "vaccines" -> R.drawable.vaccine_category
                        else -> R.drawable.no_connection
                    }
                )
                textViewQuestion.text = debunk.question
                textViewAnswer.text = debunk.answer
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebunkViewHolder {
        val binding =
            DebunkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DebunkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DebunkViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class DebunkComparator : DiffUtil.ItemCallback<Debunk>() {
        override fun areItemsTheSame(oldItem: Debunk, newItem: Debunk) =
            oldItem.question == newItem.question


        override fun areContentsTheSame(oldItem: Debunk, newItem: Debunk) =
            oldItem == newItem

    }

    fun setData(debunk: List<Debunk>) {
        this.debunkList=debunk
    }



}