package com.twoday.todaytrip.ui.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemRouteListBinding

class RouteAdapter :
    ListAdapter<RouteListData, RouteAdapter.ViewHolder>(RouteListDataDiffCallback) {

    interface ItemClick {
        fun onClick(item: RouteListData)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRouteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.position.text = (position+1).toString()
        holder.bind(currentItem)
//        if (position == itemCount - 1) {
//            holder.visi.visibility = View.INVISIBLE
//        }
    }

    inner class ViewHolder(binding: ItemRouteListBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvSavePhotoRoadText
        val address = binding.tvSavePhotoAddress
        val option = binding.ivSavePhotoOption
        val position = binding.tvSavePhotoPocketNumber
        fun bind(item: RouteListData) {
            name.text = item.name
            address.text = item.address
        }
    }
}