package com.twoday.todaytrip.ui.route

data class RouteListData(
    val contentId: String,
    val name: String,
    val address: String,
    var position: Int = 0
    )
