package com.twoday.todaytrip.ui.place_list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ItemPlaceListRecommendBinding
import com.twoday.todaytrip.ui.place_list.RecommendCover
import com.twoday.todaytrip.ui.place_list.RecommendData
import com.twoday.todaytrip.ui.place_list.RecommendEmpty
import com.twoday.todaytrip.ui.place_list.RecommendTourItem

class RecommendViewPagerAdapter : RecyclerView.Adapter<RecommendViewPagerAdapter.Holder>() {
    private val TAG = "RecommendViewPagerAdapter"

    private var recommendDataList = listOf<RecommendData>()
    var onTourItemClickListener: OnTourItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemPlaceListRecommendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int = recommendDataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        when (val currentRecommendData = recommendDataList[position]) {
            is RecommendCover -> {
                holder.bindCover(currentRecommendData)
            }

            is RecommendTourItem -> {
                holder.bindTourItem(currentRecommendData)
                holder.setOnClickListener(currentRecommendData)
            }

            is RecommendEmpty -> {
                holder.bindEmpty(currentRecommendData)
            }
        }
    }

    fun changeDataSet(newRecommendDataList: List<RecommendData>) {
        recommendDataList = newRecommendDataList
        notifyDataSetChanged()
    }

    inner class Holder(binding: ItemPlaceListRecommendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.ivItemPlaceListRecommendImage
        private val subTitleTextView: TextView = binding.tvItemPlaceListRecommendSubTitle
        private val titleTextView: TextView = binding.tvItemPlaceListRecommendTitle
        private val noResultImageView: ImageView = binding.ivItemPlaceListRecommendNoResult

        fun bindCover(recommendCover: RecommendCover) {
            Log.d(TAG,"bindCover) called")
            imageView.setImageResource(recommendCover.imageId)
            subTitleTextView.setText(recommendCover.subTitleId)
            titleTextView.text = String.format(
                itemView.context.getString(recommendCover.titleId),
                recommendCover.destination,
                recommendCover.destinationSigungu
            )
            noResultImageView.isVisible = false
        }

        fun bindTourItem(recommendTourItem: RecommendTourItem) {
            Log.d(TAG, "bindTourItem) title: ${recommendTourItem.tourItem.getTitle()}")
            recommendTourItem.tourItem.getImage().let { url ->
                Glide.with(itemView.context)
                    .load(url)
                    .into(imageView)
            }
            subTitleTextView.setText(recommendTourItem.subTitleId)
            titleTextView.text = recommendTourItem.tourItem.getTitle()
            noResultImageView.isVisible = false
        }

        fun bindEmpty(recommendEmpty: RecommendEmpty) {
            Log.d(TAG, "bindEmpty) title: ${itemView.context.getString(recommendEmpty.titleId)}")
            subTitleTextView.setText(recommendEmpty.subTitleId)
            titleTextView.setText(recommendEmpty.titleId)
            Glide.with(itemView.context)
                .load(R.drawable.gif_loading_reading_glasses)
                .into(noResultImageView)
            noResultImageView.isVisible = true
        }

        fun setOnClickListener(recommendTourItem: RecommendTourItem) {
            itemView.setOnClickListener {
                Log.d(TAG, "${recommendTourItem.tourItem.getTitle()} is clicked")
                onTourItemClickListener?.onTourItemClick(recommendTourItem.tourItem)
            }
        }
    }
}