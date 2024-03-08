package com.twoday.todaytrip.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.ui.route.RouteListData

class RouteViewModel: ViewModel() {
    private val _routeItems: MutableLiveData<List<RouteListData>> = MutableLiveData()
    val routeItems: LiveData<List<RouteListData>> = _routeItems


//    fun add(route: RouteListData) {
//        val currentList = _routeItems.value
//        currentList.add(route)
//        _routeItems.value = currentList
//    }

    fun add(photo: RouteListData) {
        val currentList = _routeItems.value
        currentList?.plus(photo)
        _routeItems.value = routeItems.value?.plus(photo)


    }
}