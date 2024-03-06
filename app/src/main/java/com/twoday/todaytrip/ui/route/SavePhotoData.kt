package com.twoday.todaytrip.ui.route

import android.net.Uri
import java.text.FieldPosition

data class SavePhotoData(
    var image: Uri,
    val name: String,
    val address: String,
    val position: Int = 0

)
