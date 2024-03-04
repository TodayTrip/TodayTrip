package com.twoday.todaytrip.ui.place_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoday.todaytrip.tourApi.AreaBasedListItem
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlaceDetailViewModel(
    private val tourItem: TourItem
): ViewModel() {

    private val currentDestination: String? = null
    private val isAddedToRoute: Boolean = false

    private val placeId: String? = null

    private var _placeItemData = MutableLiveData<TourItem>()
    val placeItemData: LiveData<TourItem> get() = _placeItemData

    private var _placeMemory = MutableLiveData<List<String>>()
    val placeMemory: LiveData<List<String>> get() = _placeMemory

    init {
        getPlaceInfo()
    }

    fun getPlaceInfo() {
        tourItem.getThumbnailImage()
        //glide-> 이미지뷰
        tourItem.getTitle()
        tourItem.getAddress()
        tourItem.getDetailInfoWithLabel()
        //리사이클러뷰 dataset
    }
}