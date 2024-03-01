package com.twoday.todaytrip.tourData

enum class TourCategoryId1(val id:String){
    NATURE("A01"),
    HUMANITIES("A02"),
    LEISURE_SPORTS("A03"),
    SHOPPING("A04"),
    FOOD("A05"),
    LODGEMENT("B02"),
    TOUR_COURSE("C01")
}
enum class TourCategoryId2(val id:String){
    // TODO 카테고리 중분류 enum class
    FOOD("A0502")
}
enum class TourCategoryId3(val id:String){
    // TODO 카테고리 소분류 enum class
    KOREAN_FOOD("A05020100"),
    WESTERN_FOOD("A05020200"),
    JAPANESE_FOOD("A05020300"),
    CHINESE_FOOD("A05020400"),
    UNIQUE_FOOD("A05020700"),
    CAFE_AND_TEA("A05020900")
}