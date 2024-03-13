package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.twoday.todaytrip.ui.route.RouteListData
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil

class RouteViewModel: ViewModel() {

    private val _routeListDataSet: MutableLiveData<List<RouteListData>> = MutableLiveData()
    val routeListDataSet: LiveData<List<RouteListData>> = _routeListDataSet

    private val _locations =  MutableLiveData<MutableList<LatLng>>()
    val locations get() = _locations


//    fun add(routeDataSet: RouteListData) {
//        val currentList = _dataSet.value?.toMutableList() ?: mutableListOf()
//        currentList.add(routeDataSet)
//        _dataSet.value = currentList
//    }

    init{
        getRouteDataSet()
    }


    private fun getRouteDataSet(){
        val contentIdList = ContentIdPrefUtil.loadContentIdList() //경로에
        val tourList = TourItemPrefUtil.loadAllTourItemList() //관광지 전부

        val loadedRouteListDataSet = mutableListOf<RouteListData>()
        contentIdList.forEach { contentId ->
            val tourItem = tourList.find { it.getContentId() == contentId }!!
            loadedRouteListDataSet.add(RouteListData(contentId, tourItem.getTitle(), tourItem.getAddress()))
        }

        _routeListDataSet.value = loadedRouteListDataSet
    }

    fun getLocation(){
        val contentIdList = ContentIdPrefUtil.loadContentIdList() //경로에
        val tourList = TourItemPrefUtil.loadAllTourItemList() //관광지 전부

        val loadedLocations = mutableListOf<LatLng>()
        contentIdList.forEach { contentId ->
            val tourItem = tourList.find { it.getContentId() == contentId }!!
            loadedLocations.add(
                LatLng(
                    tourItem.getLatitude()?.toDouble() ?: 0.0,
                    tourItem.getLongitude()?.toDouble() ?: 0.0
                )
            )

            Log.d("마커 사이즈 확인", loadedLocations.size.toString())
            Log.d("locations", _locations.value.toString())
        }

        _locations.value = loadedLocations
    }

}