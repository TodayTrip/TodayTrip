package com.twoday.todaytrip.place_list_adapter

import com.twoday.todaytrip.tourData.TourItem

interface OnTourItemClickListener {
    fun onTourItemClick(tourItem: TourItem) // TourItem 상세 페이지로 이동
}