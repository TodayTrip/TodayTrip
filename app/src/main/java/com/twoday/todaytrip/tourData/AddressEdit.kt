package com.twoday.todaytrip.tourData

import com.twoday.todaytrip.pref_utils.DestinationPrefUtil

fun String.removeDestination(): String {
    return when (DestinationPrefUtil.loadDestination()) {
        "서울" -> {
            this.replace("서울특별시 ", "")
        }
        "인천" -> {
            this.replace("인천광역시 ", "")
        }
        "전북" -> {
            this.replace("전북특별자치도 ", "")
        }
        "전남" -> {
            this.replace("전라남도 ", "")
        }
        "경북" -> {
            this.replace("경상북도 ", "")
        }
        "경남" -> {
            this.replace("경상남도 ", "")
        }
        "충북" -> {
            this.replace("충청북도 ", "")
        }
        "충남" -> {
            this.replace("충청남도 ", "")
        }
        "강원" -> {
            this.replace("강원특별자치도 ", "")
        }
        "대구" -> {
            this.replace("대구광역시 ", "")
        }
        "부산" -> {
            this.replace("부산광역시 ", "")
        }
        "대전" -> {
            this.replace("대전광역시 ", "")
        }
        "제주" -> {
            this.replace("제주특별자치도 ", "")
        }
        "경기" -> {
            this.replace("경기도 ", "")
        }
        "광주" -> {
            this.replace("광주광역시 ", "")
        }
        "울산" -> {
            this.replace("울산광역시 ", "")
        }
        else -> {
            ""
        }
    }
}