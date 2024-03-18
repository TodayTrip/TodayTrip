package com.twoday.todaytrip.ui.place_detail

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemPlaceDetailExtraInfoBinding

class PlaceInfoAdapter(private var placeInfoList: List<Pair<String,String>>) : RecyclerView.Adapter<PlaceInfoAdapter.ViewHolder>() {
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
    fun setDataSet(newPlaceInfoList: List<Pair<String,String>>){
        placeInfoList = newPlaceInfoList
        notifyDataSetChanged()
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