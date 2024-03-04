package com.twoday.todaytrip.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.tourApi.TourNetworkInterfaceUtils
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.PrefConstants
import com.twoday.todaytrip.utils.PrefConstants.DESTINATION_KEY
import com.twoday.todaytrip.utils.SharedPreferencesUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"

    private var destAreaCode: String? = null

    private val _tourInfoTabList = MutableLiveData<List<TourItem>>()
    val tourInfoTabList: LiveData<List<TourItem>>
        get() = _tourInfoTabList
    private val _restaurantTabList = MutableLiveData<List<TourItem>>()
    val restaurantTabList: LiveData<List<TourItem>>
        get() = _restaurantTabList

    private val storageRef = FirebaseStorage.getInstance().reference
    private val databaseRef = FirebaseDatabase.getInstance().reference

    init {
        loadTourItemList()
    }

    private fun loadTourItemList() {
        destAreaCode = getDestinationAreaCode(getDestination())
        if (destAreaCode.isNullOrBlank()) {
            Log.d(TAG, "loadTourITemList) error! no destination area code!")
            return
        }

        // TODO shared preference에 저장된 정보 있는지 확인하기
        // TODO 완전 랜덤인 지, 테마가 있는지 확인하기

        loadTourInfoTabList()
        loadRestaurantTabList()
        uploadData(createSampleItem())
        //loadCafeTabList()
        //loadEventPerformanceFestivalTabList()
    }

    private fun uploadData(areaBasedListItem: AreaBasedListItem) {
        // realtime database의 저장 경로
        val storagePath = "touristSites/${System.currentTimeMillis()}.jpg"
        uploadWebImageToStorage(areaBasedListItem.firstImage,storagePath)
    }

    // 코루틴을 사용하여 이미지 다운로드 및 업로드
    private fun uploadWebImageToStorage(imageUrl: String?, storagePath: String) {
        Log.d("imageUrl", imageUrl!!)
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                // 이미지 URL에서 이미지 데이터 다운로드
//                val url = URL(imageUrl)
//                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//                connection.doInput = true
//                connection.connect()
//                val inputStream: InputStream = connection.inputStream
//                val imageData = inputStream.readBytes()
//                // Firebase Storage에 이미지 업로드
//                val imageRef = storageRef.child(storagePath)
//                imageRef.putBytes(imageData).addOnSuccessListener { taskSnapshot ->
//                    // 이미지 업로드 성공 처리
//                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
//                        Log.d("imageData", downloadUri.toString())
////                        val downloadUrl = downloadUri.toString()
//                        // Realtime Database에 저장
//                        saveAreaBasedListItem(createSampleItem())
//                    }
//                }.addOnFailureListener {
//                    // 이미지 업로드 실패 처리
//                }
            } catch (e: Exception) {
                // 이미지 다운로드 또는 업로드 중 발생한 예외 처리
                withContext(Dispatchers.Main) {
                    Log.d("이미지 업로드 실패",e.message.toString())
                }
            }
        }
    }

    fun saveAreaBasedListItem(item: AreaBasedListItem) {
        // 고유한 키를 생성하여 데이터를 저장할 위치를 정합니다.
        val key = databaseRef.child("areaBasedList").push().key

        if (key != null) {
            // 'areaBasedList' 노드 아래에 새 항목을 저장합니다.
            databaseRef.child("areaBasedList").child(key).setValue(item)
                .addOnSuccessListener {
                    // 데이터 저장 성공 시 처리
                    Log.d(TAG, "Item saved successfully")
                }
                .addOnFailureListener { e ->
                    // 데이터 저장 실패 시 처리
                    Log.e(TAG, "Failed to save item", e)
                }
        }
    }

    private fun createSampleItem(): AreaBasedListItem {
        return AreaBasedListItem(
            title = "남산 서울타워",
            contentId = "126508",
            contentTypeId = "12",
            createdTime = "20210316000000",
            modifiedTime = "20210316000000",
            tel = "02-3455-9277",
            address = "서울특별시 용산구 남산공원길 105",
            addressDetail = "",
            zipcode = "04340",
            mapX = "126.988205",
            mapY = "37.551169",
            mapLevel = "1",
            areaCode = "1",
            siGunGuCode = "23",
            category1 = "A02",
            category2 = "A0201",
            category3 = "A02010600",
            firstImage = "http://tong.visitkorea.or.kr/cms/resource/76/1587376_image2_1.jpg",
            firstImageThumbnail = null,
            bookTour = "0",
            copyrightType = "C"
        )
    }
    private fun getDestination(): String? =
        SharedPreferencesUtil.loadDestination(MyApplication.appContext!!, DESTINATION_KEY) ?: null

    private fun getDestinationAreaCode(destination: String?): String? =
        if (destination == null) null
        else DestinationData.destinationAreaCodes[destination] ?: null

    private fun loadTourInfoTabList() {
        // TODO 테마가 있는 경우, 관광지 탭에 테마에 해당되는 정보만 필터링하기
        _tourInfoTabList.value = TourNetworkInterfaceUtils.getTourInfoTabList(destAreaCode!!)
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            _tourInfoTabList.value!!,
            PrefConstants.TOUR_INFO_TAB_LIST_KEY
        )
    }

    private fun loadRestaurantTabList() {
        _restaurantTabList.value = TourNetworkInterfaceUtils.getRestaurantTabList(destAreaCode!!)
        SharedPreferencesUtil.saveTourItemList(
            MyApplication.appContext!!,
            _restaurantTabList.value!!,
            PrefConstants.RESTAURANT_TAB_LIST_KEY
        )
    }
}


data class AreaBasedListItem(
    val title: String,
    val contentId: String,
    val contentTypeId: String,
    val createdTime: String,
    val modifiedTime: String,
    val tel: String = "",
    val address: String = "",
    val addressDetail: String = "",
    val zipcode: String = "",
    val mapX: String = "",
    val mapY: String = "",
    val mapLevel: String = "",
    val areaCode: String = "",
    val siGunGuCode: String = "",
    val category1: String = "",
    val category2: String = "",
    val category3: String = "",
    val firstImage: String? = null, // 이미지 URL을 nullable로 변경
    val firstImageThumbnail: String? = null,
    val bookTour: String = "",
    val copyrightType: String = ""
)