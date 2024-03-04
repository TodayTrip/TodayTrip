package com.twoday.todaytrip.utils

object PrefConstants {
    // (1) 테마/여행지 저장 Shared Preference
    const val PREFERENCE_DESTINATION_KEY = "PREFERENCE_DESTINATION_KEY"
    const val DESTINATION_KEY = "DESTINATION_KEY" // 지역
    const val THEME_KEY = "THEME_KEY" // 테마

    // (2) TourItem 목록 저장 Shared Preference
    const val PREFERENCE_TOUR_ITEM_LIST_KEY = "PREFERENCE_TOUR_ITEM_LIST_KET"
    const val TOURIST_ATTRACTION_LIST_KEY = "TOURIST_ATTRACTION_LIST_KEY" // 관광지 TourItem 목록
    const val RESTAURANT_LIST_KEY = "RESTAURANT_LIST_KEY" // 음식점 TourItem 목록
    const val CAFE_LIST_KEY = "CAFE_LIST_KEY" // 카페 TourItem 목록
    const val EVENT_LIST_KEY = "EVENT_LIST_KEY" // 행사/공연/축제 TourItem 목록

    // (3) 경로에 담은 TourItem의 contentId 목록 저장 Shared Preference
    const val PREFERENCE_CONTENT_ID_LIST_KEY = "PREFERENCE_CONTENT_ID_LIST_KEY"
    const val ADDED_CONTENT_ID_LIST_KEY = "ADDED_CONTENT_ID_LIST_KEY" // 경로에 담은 TourItem의 contentId 목록

    // (4) Record 목록 저장 Shared Preference
    const val PREFERENCE_RECORD_LIST_KEY = "PREFERENCE_RECORD_LIST_KEY"
    const val RECORD_LIST_KEY = "RECORD_LIST_KEY" // Record 목록
}