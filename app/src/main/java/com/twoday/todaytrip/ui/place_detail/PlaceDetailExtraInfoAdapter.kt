package com.twoday.todaytrip.ui.place_detail

import android.annotation.SuppressLint
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemPlaceDetailExtraInfoBinding
import com.twoday.todaytrip.tourData.TourItem

class PlaceDetailExtraInfoAdapter() : RecyclerView.Adapter<PlaceDetailExtraInfoAdapter.ViewHolder>() {

    var placeInfoList = listOf<Pair<String,String>>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPlaceDetailExtraInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = placeInfoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(placeInfoList[position])
    }

    inner class ViewHolder(binding: ItemPlaceDetailExtraInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val labelText = binding.tvItemPlaceDetailExtraInfoTitle
        private val infoText = binding.tvItemPlaceDetailExtraInfo
        fun onBind(placeInfo: Pair<String, String>) {
            labelText.text = placeInfo.first
            infoText.text = Html.fromHtml(placeInfo.second)
        }
    }
}