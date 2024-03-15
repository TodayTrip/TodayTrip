package com.twoday.todaytrip.ui.route

import androidx.recyclerview.widget.DiffUtil

object RouteListDataDiffCallback: DiffUtil.ItemCallback<RouteListData>() {
    override fun areItemsTheSame(oldItem: RouteListData, newItem: RouteListData): Boolean {
        return (oldItem.contentId == newItem.contentId)
    }

    override fun areContentsTheSame(oldItem: RouteListData, newItem: RouteListData): Boolean {
        return oldItem == newItem
    }
}