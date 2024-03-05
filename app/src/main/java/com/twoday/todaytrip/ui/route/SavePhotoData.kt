package com.twoday.todaytrip.ui.route

import android.net.Uri

data class SavePhotoData(
    var image: Uri,
    val name: String,
    val address: String,

)
