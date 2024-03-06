package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.ui.record.RecordItem
import com.twoday.todaytrip.utils.RecordPrefUtil

class RecordViewModel: ViewModel() {
    private val _recordList = MutableLiveData<List<RecordItem>>()
    val recordList: LiveData<List<RecordItem>>
        get() = _recordList

    init{
        _recordList.value = RecordPrefUtil.loadRecordList()
    }
}