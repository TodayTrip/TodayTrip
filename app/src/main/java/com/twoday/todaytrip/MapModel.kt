package com.twoday.todaytrip

import com.naver.maps.geometry.LatLng
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng

data class MapModel(
    var position: LatLng,
    var index: Int? = null
) : TedClusterItem {
    override fun getTedLatLng(): TedLatLng {
        return TedLatLng(position.latitude, position.longitude)
    }

    var title: String? = null
    var snippet: String? = null
    constructor(lat: Double, lng: Double) : this(LatLng(lat, lng)) {
        title = null
        snippet = null
    }

    constructor(lat: Double, lng: Double, title: String?, snippet: String?) : this(
        LatLng(lat, lng)
    ) {
        this.title = title
        this.snippet = snippet
    }
}