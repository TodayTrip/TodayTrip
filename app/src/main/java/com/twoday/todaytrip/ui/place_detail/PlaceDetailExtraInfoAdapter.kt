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

class PlaceDetailExtraInfoAdapter(
    private val placeInfoList: List<Pair<String, String>>
) : RecyclerView.Adapter<PlaceDetailExtraInfoAdapter.ViewHolder>() {
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


    inner class ViewHolder(private val binding: ItemPlaceDetailExtraInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val labelText = binding.tvItemPlaceDetailExtraInfoTitle
        private val infoText = binding.tvItemPlaceDetailExtraInfo
        fun onBind(placeInfo: Pair<String, String>) {
            labelText.text = placeInfo.first
            Log.d("placeDetailInfo", "placeInfo = ${placeInfo.second}")
            infoText.text = Html.fromHtml(placeInfo.second)
//            infoText.text = placeInfo.second
        }
    }
}