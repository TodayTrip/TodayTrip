package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.twoday.todaytrip.ui.route.RouteListData
import com.twoday.todaytrip.pref_utils.ContentIdPrefUtil
import com.twoday.todaytrip.pref_utils.TourItemPrefUtil

class RouteViewModel : ViewModel() {

    private val _routeListDataSet: MutableLiveData<List<RouteListData>> = MutableLiveData()
    val routeListDataSet: LiveData<List<RouteListData>> = _routeListDataSet

    private val _editMode: MutableLiveData<Boolean> = MutableLiveData()
    val editMode: LiveData<Boolean> = _editMode

    private val _locations = MutableLiveData<MutableList<LatLng>>()
    val locations get() = _locations

    private val _isMapReady = MutableLiveData<Boolean>(false)
    val isMapReady: LiveData<Boolean> = _isMapReady


//    fun add(routeDataSet: RouteListData) {
//        val currentList = _dataSet.value?.toMutableList() ?: mutableListOf()
//        currentList.add(routeDataSet)
//        _dataSet.value = currentList
//    }

    init {
        getRouteDataSet()
    }

    fun toggleEditMode() {
        _editMode.value = _editMode.value != true
        Log.d("sdc", "viewmodel ${_editMode.value.toString()}")
    }

    fun dataRemove(item: RouteListData, position: Int) {
        val list = _routeListDataSet.value!!.toMutableList()
        list.remove(item)
        ContentIdPrefUtil.removeContentId(item.contentId)
        _routeListDataSet.value = list
    }

    fun dataAllRemove() {
        val list = _routeListDataSet.value!!.toMutableList()
        list.clear()
        ContentIdPrefUtil.resetContentIdListPref()
        _routeListDataSet.value = list
    }

    fun getRouteDataSet() {
        val contentIdList = ContentIdPrefUtil.loadContentIdList() //담은 목록
        val tourList = TourItemPrefUtil.loadAllTourItemList() //관광지 전부


        val loadedRouteListDataSet = mutableListOf<RouteListData>()
        contentIdList.forEach { contentId ->
            val tourItem = tourList.find { it.getContentId() == contentId }!!
            loadedRouteListDataSet.add(
                RouteListData(
                    contentId,
                    tourItem.getTitle(),
                    tourItem.getAddress()
                )
            )
        }

        _routeListDataSet.value = loadedRouteListDataSet
    }

    fun updateRouteDataSet() {
        val contentIdList = ContentIdPrefUtil.loadContentIdList() //담은 목록
        val tourList = TourItemPrefUtil.loadAllTourItemList() //관광지 전부

        val updatedRouteListDataSet =
            _routeListDataSet.value?.toMutableList()
        updatedRouteListDataSet?.let {
            contentIdList.forEachIndexed { index, contentId ->
                if (it[index].contentId != contentId) {
                    val tourItem = tourList.find {
                            item -> item.getContentId() == contentId
                    }!!
                    it[index] = RouteListData(
                        contentId,
                        tourItem.getTitle(),
                        tourItem.getAddress()
                    )
                }
            }
            _routeListDataSet.value = it
        }
    }

    fun getLocation() {
        val contentIdList = ContentIdPrefUtil.loadContentIdList() //경로에
        val tourList = TourItemPrefUtil.loadAllTourItemList() //관광지 전부

        val loadedLocations = mutableListOf<LatLng>()
        contentIdList.forEach { contentId ->
            val tourItem = tourList.find { it.getContentId() == contentId }?.let {
                loadedLocations.add(
                    LatLng(
                        it.getLatitude()?.toDouble() ?: 0.0,
                        it.getLongitude()?.toDouble() ?: 0.0
                    )
                )
            }

            Log.d("마커 사이즈 확인", loadedLocations.size.toString())
            Log.d("locations", _locations.value.toString())
        }

        _locations.value = loadedLocations
    }

    fun setIsMapReady(isReady: Boolean) {
        _isMapReady.value = isReady
    }
}