package com.twoday.todaytrip.ui.place_list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ItemPlaceListRecommendBinding
import com.twoday.todaytrip.databinding.ItemRouteListBinding

class PlaceInfiniteAdapter (var list: MutableList<Int>) : RecyclerView.Adapter<PlaceInfiniteAdapter.InfiniteViewHolder>() {

    inner class InfiniteViewHolder(val binding: ItemPlaceListRecommendBinding) : RecyclerView.ViewHolder(binding.root) {
        var imageView = binding.ivItemPlaceListRecommendImage

        fun onBind(res: Int) {
            imageView.setImageResource(res)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfiniteViewHolder {
        val binding =
            ItemPlaceListRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfiniteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfiniteViewHolder, position: Int) {
        holder.onBind(list[position % list.size]) // position에 실제 배너의 개수를 나눈 나머지 값을 사용한다. (여기서는 3개라 하드코딩으로 3을 넣음)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE // 아이템의 갯수를 임의로 확 늘린다.
}