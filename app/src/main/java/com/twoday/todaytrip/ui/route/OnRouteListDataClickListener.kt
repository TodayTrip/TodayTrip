package com.twoday.todaytrip.ui.route

interface OnRouteListDataClickListener {
    fun onRouteListDataClick(contentId: String)

    fun onRouteListDataRemove(item: RouteListData,position: Int)
}