package com.twoday.todaytrip.ui.place_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.tourData.TourItem

class PlaceDetailViewModel: ViewModel() {

    private var _placeItemData = MutableLiveData<TourItem>()
    val placeItemData: LiveData<TourItem> get() = _placeItemData

    private var _placeMemory = MutableLiveData<>()
}