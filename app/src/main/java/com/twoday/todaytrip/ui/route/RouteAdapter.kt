package com.twoday.todaytrip.ui.route

import android.icu.text.Transliterator.Position
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemRouteListBinding


class RouteAdapter :
    ListAdapter<RouteListData, RouteAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<RouteListData>() {
        override fun areItemsTheSame(oldItem: RouteListData, newItem: RouteListData): Boolean {
            // 비디오 id가 같은지 확인
            return (oldItem.num == newItem.num)
        }

        override fun areContentsTheSame(oldItem: RouteListData, newItem: RouteListData): Boolean {
            // 모든 필드가 같은지 확인 (data class의 equals 사용)
            return oldItem == newItem
        }
    }) {

    interface ItemClick {
        fun onClick(item: RouteListData)
    }

    var itemClick: ItemClick? = null

    inner class ViewHolder(binding: ItemRouteListBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvSavePhotoRoadText
        val num = binding.tvSavePhotoPocketNumber
        val visi = binding.viRouteLastView
        val address = binding.tvSavePhotoAddress
        val option = binding.ivSavePhotoOption
        fun bind(item: RouteListData) {
            name.text = item.name
            address.text = item.address
//            cancel.isVisible = item.cancel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRouteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.name.text = currentItem.name
        holder.num.text = (position+1).toString()
        holder.bind(getItem(position))
//        notifyItemMoved()

//        holder.cancel.isVisible = !currentItem.cancel //홀더에 아이콘이 visivle인지 확인하고 클릭시 boolean반대값을 적용
//        item.favorite = !item.favorite //클릭시 boolean 값변경

        if (position == itemCount - 1) {
            holder.visi.visibility = View.INVISIBLE
        }
    }
    private fun notifyItemMoved() {
    }

    fun editVisiblity() {

    }
}