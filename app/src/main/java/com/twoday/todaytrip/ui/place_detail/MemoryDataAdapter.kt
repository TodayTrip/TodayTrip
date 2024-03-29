package com.twoday.todaytrip.ui.place_detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemPlaceDetailMyMemoryBinding
import com.twoday.todaytrip.utils.glide

class MemoryDataAdapter(): ListAdapter<MemoryData, MemoryDataAdapter.ViewHolder>(MemoryDataDiffCallback) {
    private val TAG = "MyMemoryAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPlaceDetailMyMemoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memoryData = getItem(position)
        holder.bind(memoryData)
    }

    override fun submitList(list: MutableList<MemoryData>?) {
        Log.d(TAG, "submitList) called, currentList: ${currentList}")
        super.submitList(list)
        Log.d(TAG, "super.submitList) called, currentList: ${currentList}")
    }

    inner class ViewHolder(binding: ItemPlaceDetailMyMemoryBinding): RecyclerView.ViewHolder(binding.root) {
        private val travelDateTextView: TextView = binding.tvItemPlaceDetailMyMemoryTime
        private val memoryImageView: ImageView = binding.ivItemPlaceDetailMyMemory

        fun bind(memoryData: MemoryData) {
            Log.d(TAG, "bind) called")
            travelDateTextView.text = memoryData.date

            memoryImageView.glide(memoryData.url)
            memoryImageView.clipToOutline = true
        }
    }
}