package com.twoday.todaytrip.ui.place_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ItemPlaceDetailNearbyBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemClickListener
import com.twoday.todaytrip.utils.glideWithPlaceholder

class NearByAdapter : RecyclerView.Adapter<NearByAdapter.Holder>(){

    private var nearByList = listOf<TourItem>()
    var onTourItemClickListener: OnTourItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemPlaceDetailNearbyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int = nearByList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentTourItem = nearByList[position]
        holder.bind(currentTourItem)
        holder.setClickListener(currentTourItem)
    }

    fun changeDataSet(newNearByList: List<TourItem>){
        nearByList = newNearByList
        notifyDataSetChanged()
    }

    inner class Holder(binding: ItemPlaceDetailNearbyBinding): RecyclerView.ViewHolder(binding.root){
        private val imageView: ImageView = binding.ivItemPlaceDetailNearby
        private val titleTextView: TextView = binding.tvItemPlaceDetailNearbyTitle

        fun bind(tourItem: TourItem){
            tourItem.getImage()?.let{
                imageView.glideWithPlaceholder(it)
            }
            imageView.clipToOutline = true

            titleTextView.text = tourItem.getTitle()
        }
        fun setClickListener(tourItem:TourItem){
            itemView.setOnClickListener {
                onTourItemClickListener?.onTourItemClick(tourItem)
            }
        }
    }
}