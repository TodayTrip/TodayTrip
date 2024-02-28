package com.twoday.todaytrip.ui.route

data class RouteListData(
    val name: String,
    val num: Int,
    val cancel: Boolean = false,
    val option: Boolean = false
)
