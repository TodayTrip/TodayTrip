package com.twoday.todaytrip.ui.place_list.adapter

import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.pref_utils.ContentIdPrefUtil

object OnTourItemAddClickListener {
    fun onTourItemAddClick(tourItem:TourItem){
        tourItem.isAdded = !tourItem.isAdded
        if(tourItem.isAdded)
            ContentIdPrefUtil.addContentId(tourItem.getContentId())
        else
            ContentIdPrefUtil.removeContentId(tourItem.getContentId())
    }
}