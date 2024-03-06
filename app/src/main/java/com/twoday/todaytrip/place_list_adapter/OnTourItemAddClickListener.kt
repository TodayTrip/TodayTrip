package com.twoday.todaytrip.place_list_adapter

import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.ContentIdPrefUtil

object OnTourItemAddClickListener {
    fun onTourItemAddClick(tourItem:TourItem){
        tourItem.isAdded = !tourItem.isAdded
        if(tourItem.isAdded)
            ContentIdPrefUtil.addContentId(tourItem.getContentId())
        else
            ContentIdPrefUtil.removeContentId(tourItem.getContentId())
    }
}