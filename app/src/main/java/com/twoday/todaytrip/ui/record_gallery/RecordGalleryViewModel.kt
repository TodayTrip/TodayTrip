package com.twoday.todaytrip.ui.record_gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordGalleryViewModel : ViewModel() {
    val imageUriListLiveData: MutableLiveData<List<String>> = MutableLiveData()

    fun setImageUriList(imageUriList: List<String>) {
        imageUriListLiveData.value = imageUriList
    }

}