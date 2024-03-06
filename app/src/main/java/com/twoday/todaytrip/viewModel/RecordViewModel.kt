package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.ui.record.Record
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.RecordPrefUtil

class RecordViewModel: ViewModel() {
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
    fun getChartDataList():List<Pair<String, Float>>{
        var chartDataList = mutableListOf<Pair<String, Float>>()
        DestinationData.allRandomDestination.forEachIndexed { index, destination ->
            chartDataList.add(
                destination to _recordList.value!!.filter {
                    it.destination == destination
                }.size.toFloat()
            )
            Log.d(TAG, "$destination ${chartDataList[index].second}")
        }
        chartDataList = chartDataList.filter{ it.second > 0 }.sortedBy { it.second }.toMutableList()
        // TODO 방문 횟수 같은 여행지 -> 00 외 3 으로 표시될 수 있도록 바꾸기

        val chartSize = chartDataList.size
        return when{
            (chartSize == 0) -> emptyList()
            (chartSize <= 3) -> chartDataList.subList(0, chartSize)
            else -> chartDataList.subList(0, 3)
        }
    }
}