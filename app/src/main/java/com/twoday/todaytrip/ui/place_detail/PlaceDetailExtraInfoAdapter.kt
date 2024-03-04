package com.twoday.todaytrip.ui.place_detail

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.tourData.TourItem

class PlaceDetailExtraInfoAdapter: ListAdapter<TourItem, RecyclerView.ViewHolder>(object :
    DiffUtil.ItemCallback<TourItem>() {
    override fun areItemsTheSame(oldItem: TourItem, newItem: TourItem): Boolean =
        oldItem.getContentId() == newItem.getContentId()

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TourItem, newItem: TourItem): Boolean =
        oldItem == newItem
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}