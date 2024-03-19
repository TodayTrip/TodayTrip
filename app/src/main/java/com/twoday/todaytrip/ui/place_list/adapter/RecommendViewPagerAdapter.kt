package com.twoday.todaytrip.ui.place_list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.databinding.ItemPlaceListRecommendBinding
import com.twoday.todaytrip.ui.place_list.RecommendCover
import com.twoday.todaytrip.ui.place_list.RecommendData
import com.twoday.todaytrip.ui.place_list.RecommendTourItem

class RecommendViewPagerAdapter : RecyclerView.Adapter<RecommendViewPagerAdapter.Holder>() {
    private val TAG = "RecommendViewPagerAdapter"

    private var recommendDataList = listOf<RecommendData>()
    var onTourItemClickListener: OnTourItemClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return recommendDataList[position].getViewType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemPlaceListRecommendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int = recommendDataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        when(val currentRecommendData = recommendDataList[position]){
            is RecommendCover ->{
                holder.bindCover(currentRecommendData)
            }
            is RecommendTourItem -> {
                holder.bindTourItem(currentRecommendData)
                holder.setOnClickListener(currentRecommendData)
            }
        }
    }

    inner class Holder(binding: ItemPlaceListRecommendBinding): RecyclerView.ViewHolder(binding.root){
        private val imageView: ImageView = binding.ivItemPlaceListRecommendImage
        private val subTitleTextView: TextView = binding.tvItemPlaceListRecommendSubTitle
        private val titleTextView: TextView = binding.tvItemPlaceListRecommendTitle

        fun bindCover(recommendCover: RecommendCover){
            imageView.setImageResource(recommendCover.imageId)
            subTitleTextView.setText(recommendCover.subTitleId)
            titleTextView.text = recommendCover.title
        }
        fun bindTourItem(recommendTourItem: RecommendTourItem){
            Glide.with(MyApplication.appContext!!)
                .load(recommendTourItem.imageUrl)
                .into(imageView)
            subTitleTextView.setText(recommendTourItem.subTitleId)
            titleTextView.text = recommendTourItem.title
        }
        fun setOnClickListener(recommendTourItem: RecommendTourItem){
            itemView.setOnClickListener {
                Log.d(TAG, "${recommendTourItem.title} is clicked")
                onTourItemClickListener?.onTourItemClick(recommendTourItem.tourItem)
            }
        }
    }
}