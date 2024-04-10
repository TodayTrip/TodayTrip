package com.twoday.todaytrip.utils

import com.naver.maps.geometry.LatLng

object DestinationData {
    val destinationAreaCodes = mapOf(
        "서울" to "1",
        "인천" to "2",
        "대전" to "3",
        "대구" to "4",
        "광주" to "5",
        "부산" to "6",
        "울산" to "7",
        "세종" to "8",
        "경기" to "31",
        "강원" to "32",
        "충북" to "33",
        "충남" to "34",
        "경북" to "35",
        "경남" to "36",
        "전북" to "37",
        "전남" to "38",
        "제주" to "39"
    )
    val destinationLatLng = mapOf(
        "서울" to LatLng(37.566535, 126.9779692),
        "인천" to LatLng(37.4562557, 126.7052062),
        "대전" to LatLng(36.3504119, 127.3845475),
        "대구" to LatLng(35.8714354, 128.601445),
        "부산" to LatLng(35.1795543, 129.0756416),
        "울산" to LatLng(35.5383773, 129.3113596),
        "경기" to LatLng(37.4138, 127.5183),
        "강원" to LatLng(37.8228, 128.1555),
        "충북" to LatLng(36.6359, 127.4913),
        "충남" to LatLng(36.6588, 126.6728),
        "광주" to LatLng(35.1595454, 126.8526012),
        "경북" to LatLng(36.4919, 128.8889),
        "경남" to LatLng(35.4606, 128.2132),
        "전북" to LatLng(35.7175, 127.153),
        "전남" to LatLng(34.8679, 126.991),
        "제주" to LatLng(33.4996213, 126.5311884)
    )
}
