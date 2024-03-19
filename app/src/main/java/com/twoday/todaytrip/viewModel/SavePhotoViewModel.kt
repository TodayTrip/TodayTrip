package com.twoday.todaytrip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoday.todaytrip.ui.save_photo.SavePhotoData
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil

class SavePhotoViewModel: ViewModel() {

    private val _savePhotoDataList: MutableLiveData<List<SavePhotoData>> = MutableLiveData()
    val savePhotoDataList: LiveData<List<SavePhotoData>> = _savePhotoDataList

    init {
        savePhotoData()
    }

    fun savePhotoData(){
        val allTourItemList = TourItemPrefUtil.loadAllTourItemList()
        val addedContentIdList = ContentIdPrefUtil.loadContentIdList()
        val savePhotoData = mutableListOf<SavePhotoData>()

        addedContentIdList.forEach { contentID ->
            val tourItem = allTourItemList.find {
                it.getContentId() == contentID
            }!!
            savePhotoData.add(SavePhotoData(tourItem))
            if (allTourItemList.isNotEmpty()) {
//                binding.layoutRouteEmptyFrame.visibility = View.INVISIBLE
            }
        }
        _savePhotoDataList.value = savePhotoData
    }

    fun addImage(position: Int, imageList: MutableList<String>){
        _savePhotoDataList.value?.get(position)?.imageUriList = imageList
    }
}