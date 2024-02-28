package com.twoday.todaytrip.ui.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemRouteListBinding


class RouteAdapter :
    ListAdapter<RouteListData, RouteAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<RouteListData>() {
        override fun areItemsTheSame(oldItem: RouteListData, newItem: RouteListData): Boolean {
            // 비디오 id가 같은지 확인
            return (oldItem.name == newItem.name) && (oldItem.num == newItem.num)
        }

        override fun areContentsTheSame(oldItem: RouteListData, newItem: RouteListData): Boolean {
            // 모든 필드가 같은지 확인 (data class의 equals 사용)
            return oldItem == newItem
        }
    }) {

    inner class ViewHolder(binding: ItemRouteListBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        val name = binding.tvRoadText
        val num = binding.tvRoutePocketNumber
        val visi = binding.viRouteLastView
        fun bind(item: RouteListData) {
            name.text = item.name
        }

        override fun onClick(v: View?) {
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
        holder.num.text = holder.adapterPosition.toString()
        notifyItemMoved()
        holder.bind(getItem(position))

        if (position == itemCount - 1) {
            holder.visi.visibility = View.INVISIBLE
        }
    }

    private fun notifyItemMoved() {

    }

}