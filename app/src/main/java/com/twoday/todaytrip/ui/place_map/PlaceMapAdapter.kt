package com.twoday.todaytrip.ui.place_map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.PlaceData
import com.twoday.todaytrip.databinding.ItemPlaceMapEachBinding
import com.twoday.todaytrip.tourData.TourItem

class PlaceMapAdapter : ListAdapter<PlaceData, PlaceMapAdapter.ViewHolder>(object : DiffUtil.ItemCallback<PlaceData>() {

    var placeList = mutableListOf<TourItem>()


    override fun areItemsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean =
        oldItem.placeId == newItem.placeId


    override fun areContentsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean =
        oldItem == newItem

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPlaceMapEachBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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