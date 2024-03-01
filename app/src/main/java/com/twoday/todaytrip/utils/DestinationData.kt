package com.twoday.todaytrip.utils
object DestinationData {
    val allRandomDestination = listOf(
        "서울",
        "인천",
        "전북",
        "전남",
        "경북",
        "경남",
        "충북",
        "충남",
        "강원",
        "대구",
        "부산",
        "대전",
        "제주",
        "경기",
        "광주",
        "울산"
    )

    val themeDestinations = mapOf(
        "산" to listOf("경기", "강원", "경북", "경남", "전남", "제주"),
        "바다" to listOf("인천", "경기", "충남", "경남", "전남", "제주"),
        "역사" to listOf("서울", "경기", "강원", "충북", "충남", "경북", "경남", "전북", "전남"),
        "휴양" to listOf("서울", "경기", "강원", "경북", "경남", "전남"),
        "체험" to listOf("서울", "경기", "강원", "충남", "경북", "경남", "전북", "전남"),
        "레포츠" to listOf("경기", "강원", "충북", "경북", "경남"),
        "문화시설" to listOf("서울", "경기", "강원", "경남", "전남")
    )
    
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
}
