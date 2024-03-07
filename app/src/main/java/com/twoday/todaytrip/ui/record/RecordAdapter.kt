package com.twoday.todaytrip.ui.record

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.twoday.todaytrip.databinding.ItemRecordBinding

class RecordAdapter: ListAdapter<Record, RecordAdapter.Holder>(RecordDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Holder(binding: ItemRecordBinding): ViewHolder(binding.root){
        val imageView: ImageView = binding.ivItemRecordImage
        val titleTextView: TextView = binding.tvItemRecordTitle
        val dateTextView: TextView = binding.tvItemRecordDate

        fun bind(record:)
    }
}