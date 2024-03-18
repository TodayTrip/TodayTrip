package com.twoday.todaytrip.ui.place_detail

import androidx.recyclerview.widget.DiffUtil

object MemoryDataDiffCallback : DiffUtil.ItemCallback<MemoryData>() {
    override fun areItemsTheSame(oldItem: MemoryData, newItem: MemoryData): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: MemoryData, newItem: MemoryData): Boolean {
        return oldItem == newItem
    }
}