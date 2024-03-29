package com.twoday.todaytrip.ui.place_list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.twoday.todaytrip.tourData.TourItem

object TourItemDiffCallback : DiffUtil.ItemCallback<TourItem>() {
    override fun areItemsTheSame(oldItem: TourItem, newItem: TourItem): Boolean {
        return oldItem.getContentId() == newItem.getContentId()
    }

    override fun areContentsTheSame(oldItem: TourItem, newItem: TourItem): Boolean {
        return oldItem.isAdded == newItem.isAdded
    }
}