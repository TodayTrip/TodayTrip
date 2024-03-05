package com.twoday.todaytrip.ui.place_map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoday.todaytrip.PlaceData
import com.twoday.todaytrip.tourData.TourItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PlaceMapViewModel : ViewModel() {

    private val currentDestination: String? = null
    private val isAddedToRoute: Boolean = false

//    private val _placeCardList: MutableLiveData<List<TourItem>> =
//        MutableLiveData(List(10) { TourItem() })
//    val placeCardList: LiveData<List<TourItem>>
//        get() = _placeCardList

    init {
        updatePlaceList()
    }

//    private fun getPlaceData(): List<TourItem> {
//    }

    private fun updatePlaceList() = viewModelScope.launch(Dispatchers.IO) {
        try {
//            val result = getPlaceData()
//            _placeCardList.postValue(
//                result.map {  }
//            )

        } catch (e: Exception) {
            Log.e("PlaceMapViewModel", "place list error = ${e.message}", e)
        }
    }
}