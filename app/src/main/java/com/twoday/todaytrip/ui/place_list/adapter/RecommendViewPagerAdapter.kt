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
import com.twoday.todaytrip.ui.place_list.RecommendData

class RecommendViewPagerAdapter : RecyclerView.Adapter<RecommendViewPagerAdapter.Holder>() {
    private val TAG = "RecommendViewPagerAdapter"

    private var recommendTourItemList = listOf<RecommendData>()
    var onTourItemClickListener: OnTourItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemPlaceListRecommendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int = recommendTourItemList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentRecommendTourItem = recommendTourItemList[position]
        holder.bind(currentRecommendTourItem)
        holder.setOnClickListener(currentRecommendTourItem)
    }

    inner class Holder(binding: ItemPlaceListRecommendBinding): RecyclerView.ViewHolder(binding.root){
        private val imageView: ImageView = binding.ivItemPlaceListRecommendImage
        private val subTitleTextView: TextView = binding.tvItemPlaceListRecommendSubTitle
        private val titleTextView: TextView = binding.tvItemPlaceListRecommendTitle

        fun bind(recommendData: RecommendData){
            Glide.with(MyApplication.appContext!!)
                .load(recommendData.imageUri)
                .into(imageView)
            subTitleTextView.text = recommendData.subTitle
            titleTextView.text = recommendData.title
        }
        fun setOnClickListener(recommendData: RecommendData){
            if(recommendData.tourItem == null) return

            itemView.setOnClickListener {
                Log.d(TAG, "${recommendData.title} is clicked")
                onTourItemClickListener?.onTourItemClick(recommendData.tourItem)
            }
        }
    }
}