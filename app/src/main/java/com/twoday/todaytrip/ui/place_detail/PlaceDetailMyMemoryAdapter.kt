package com.twoday.todaytrip.ui.place_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.twoday.todaytrip.databinding.ItemPlaceDetailMyMemoryBinding

class PlaceDetailMyMemoryAdapter: ListAdapter<MemoryData, PlaceDetailMyMemoryAdapter.ViewHolder>(object : DiffUtil.ItemCallback<MemoryData>() {
    override fun areItemsTheSame(oldItem: MemoryData, newItem: MemoryData): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: MemoryData, newItem: MemoryData): Boolean {
        return oldItem == newItem
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPlaceDetailMyMemoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind()
    }

    inner class ViewHolder(private val binding: ItemPlaceDetailMyMemoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind() {

        }
    }
}