package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.twoday.todaytrip.ui.place_map.LocationInfo
import com.twoday.todaytrip.utils.TourItemPrefUtil

class PlaceMapViewModel : ViewModel() {

    private val _locations = MutableLiveData<List<LocationInfo>>()
    val locations: LiveData<List<LocationInfo>>
        get() = _locations

    private val _isMapReady = MutableLiveData<Boolean>(false)
    val isMapReady: LiveData<Boolean> get() = _isMapReady

    init {
        loadTouristAttractionLocations()
    }

    fun onTabSeleted(position: Int) {
        when (position) {
            0 -> loadTouristAttractionLocations()
            1 -> loadRestaurantLocations()
            2 -> loadCafeLocations()
            3 -> loadEventLocations()
        }
    }

    private fun loadTouristAttractionLocations() {
        val newLocations = TourItemPrefUtil.loadTouristAttractionList().map {
            LocationInfo(
                LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble()),
                it.getTitle()
            )
        }
        _locations.value = newLocations
    }

    private fun loadRestaurantLocations() {
        val newLocations = TourItemPrefUtil.loadRestaurantList().map {
            LocationInfo(
                LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble()),
                it.getTitle()
            )
        }
        _locations.value = newLocations
    }

    private fun loadCafeLocations() {
        val newLocations = TourItemPrefUtil.loadCafeList().map {
            LocationInfo(
                LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble()),
                it.getTitle()
            )
        }
        _locations.value = newLocations
    }

    private fun loadEventLocations() {
        val newLocations = TourItemPrefUtil.loadEventList().map {
            LocationInfo(
                LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble()),
                it.getTitle()
            )
        }
        _locations.value = newLocations
    }

    fun setIsMapReady(isMapReady: Boolean){
        _isMapReady.value = isMapReady
    }
}