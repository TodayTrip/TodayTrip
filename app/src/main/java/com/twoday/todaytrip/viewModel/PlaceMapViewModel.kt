package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoday.todaytrip.PlaceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PlaceMapViewModel : ViewModel() {

    private val _placeCardList: MutableLiveData<List<PlaceData>> =
        MutableLiveData(List(10) { PlaceData() })
    val placeCardList: LiveData<List<PlaceData>>
        get() = _placeCardList

    init {
        updatePlaceList()
    }

    private fun getPlaceData(): List<PlaceData> {
        TODO("Not yet implemented")
    }

    private fun updatePlaceList() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = getPlaceData()
//            _placeCardList.postValue(
//                result.map {  }
//            )

        } catch (e: Exception) {
            Log.e("PlaceMapViewModel", "place list error = ${e.message}", e)
        }
    }
}