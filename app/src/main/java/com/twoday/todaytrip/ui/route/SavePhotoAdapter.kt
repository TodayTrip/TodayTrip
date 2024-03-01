package com.twoday.todaytrip.ui.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemRouteListBinding
import com.twoday.todaytrip.databinding.ItemSavePhotoListBinding

class SavePhotoAdapter(private val item: MutableList<RouteListData>) :
    RecyclerView.Adapter<SavePhotoAdapter.itemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavePhotoAdapter.itemViewHolder {
        val binding =
            ItemSavePhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return itemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavePhotoAdapter.itemViewHolder, position: Int) {
        if (position == itemCount - 1) {
            holder.visi.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class itemViewHolder(binding: ItemSavePhotoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvSavePhotoRoadText
        val visi = binding.viRouteLastView
        fun bind(item: RouteListData) {

        }
    }
}