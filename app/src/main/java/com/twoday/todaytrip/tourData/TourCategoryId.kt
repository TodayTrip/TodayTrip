package com.twoday.todaytrip.tourData

enum class TourCategoryId1(val id:String){
    NATURE("A01"), // 자연
    HUMANITIES("A02"), // 인문
    LEISURE_SPORTS("A03"), // 레포츠
    SHOPPING("A04"), // 쇼핑
    FOOD("A05"), // 음식점
    LODGEMENT("B02"), // 숙소
    TOUR_COURSE("C01") // 추천 여행 코스
}
enum class TourCategoryId2(val id:String){
    NATURE_TOURIST_ATTRACTION("A0101"), // 자연>자연관광지

    FOOD("A0502") // 음식점>음식점
}
enum class TourCategoryId3(val id:String){
    MOUNTAIN("A01010400"), // 자연>자연관광지>산
    NATURAL_RECREATION_FOREST("A01010600"), // 자연>자연관광지>자연휴양림
    ARBORETUM("A01010700"), //자연>자연관광지>수목원

    COASTAL_SCENERY("A0101100"), // 자연>자연관광지>해안절경
    PORT("A01011400"), // 자연>자연관광지>항구/포구
    ISLAND("A01011300"), // 자연>자연관광지>섬
    LIGHTHOUSE("A01011600"), // 자연>자연관광지>등대
    BEACH("A01011200"), // 자연>자연관광지>해수욕장

    KOREAN_FOOD("A05020100"), // 음식점>음식점>한식
    WESTERN_FOOD("A05020200"), // 음식점>음식점>양식
    JAPANESE_FOOD("A05020300"), // 음식점>음식점>일식
    CHINESE_FOOD("A05020400"), // 음식점>음식점>중식
    UNIQUE_FOOD("A05020700"), // 음식점>음식점>이색 음식
    CAFE_AND_TEA("A05020900") // 음식점>음식점>카페/정통 찻집
}