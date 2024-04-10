package com.twoday.todaytrip.ui.record

import android.os.Parcelable
import com.twoday.todaytrip.ui.save_photo.SavePhotoData
import com.twoday.todaytrip.utils.DateTimeUtil
import com.twoday.todaytrip.pref_utils.DestinationPrefUtil
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Parcelize
data class Record(
    val recordId: String = UUID.randomUUID().toString(),
    val destination:String = DestinationPrefUtil.loadDestination(),
    val travelDate:String = DateTimeUtil.getCurrentDate(),
    val savePhotoDataList: List<SavePhotoData>
):Parcelable