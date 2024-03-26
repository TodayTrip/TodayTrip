package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.ui.record.Record
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.RecordPrefUtil

class RecordViewModel: ViewModel(){
    private val TAG = "RecordViewModel"

    private val _recordList = MutableLiveData<List<Record>>()
    val recordList: LiveData<List<Record>>
        get() = _recordList

    init{
        loadRecordList()
    }

    fun loadRecordList(){
        _recordList.value = RecordPrefUtil.loadRecordList()
        Log.d(TAG, "loadRecordList) record list size: ${recordList.value?.size}")
    }
    fun getChartDataList():List<String> = _recordList.value!!.map { it.destination }
//        DestinationData.allRandomDestination.forEach { destination ->
//            if (_recordList.value!!.any { it.destination == destination }) {
//                chartDataList.add(destination)
//            }
//        }
//
//        chartDataList.forEach {
//            Log.d(TAG, "chartDataList = $it")
//        }
//
//        val chartSize = chartDataList.size
//        return when{
//            (chartSize == 0) -> emptyList()
//            (chartSize <= 3) -> chartDataList.subList(0, chartSize)
//            else -> chartDataList.subList(0, chartSize)
//        }

}