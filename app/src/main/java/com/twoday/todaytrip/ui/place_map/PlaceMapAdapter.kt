package com.twoday.todaytrip.ui.place_map

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.PlaceData
import com.twoday.todaytrip.databinding.ItemPlaceMapEachBinding

class PlaceMapAdapter : ListAdapter<PlaceData, PlaceMapAdapter.ViewHolder>(object : DiffUtil.ItemCallback<PlaceData>() {
    override fun areItemsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean =
        oldItem.placeId == newItem.placeId


    override fun areContentsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean =
        oldItem == newItem

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

//    override fun getItemCount(): Int {
//    }

    inner class ViewHolder(private val binding: ItemPlaceMapEachBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun onBind(item: PlaceData) {

            }

    }
}