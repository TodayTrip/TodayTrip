package com.twoday.todaytrip.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.ui.route.RouteListData

class RouteViewModel: ViewModel() {
    private val _routeItems: MutableLiveData<RouteListData> = MutableLiveData()
    val routeItems: LiveData<RouteListData> = _routeItems


//    fun add(route: RouteListData) {
//        val currentList = _routeItems.value
//        currentList.add(route)
//        _routeItems.value = currentList
//    }

    fun add(photo: RouteListData) {
        val currentList = _routeItems.value


    }
}