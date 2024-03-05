package com.twoday.todaytrip.tourData

import android.util.Log
import com.twoday.todaytrip.tourApi.AreaBasedListItem
import com.twoday.todaytrip.tourApi.IntroDetailItem

data class TouristDestination(
    private val _tourItemInfo: AreaBasedListItem,
    private val touristDestinationInfo: IntroDetailItem
) : TourItem(tourItemInfo = _tourItemInfo) {
    override fun getTimeInfoWithLabel(): List<Pair<String, String>> {
        Log.d("TouristDestination", "getTimeInfoWithLabel()")
        return listOf(
            ("이용 시간" to touristDestinationInfo.usetime ?: "정보 없음") as Pair<String, String>,
            ("쉬는날" to touristDestinationInfo.restdate ?: "정보 없음") as Pair<String, String>
        )
    }

    override fun getDetailInfoWithLabel(): List<Pair<String, String>> {
        val detailInfoWithLabel = mutableListOf<Pair<String, String>>()
        detailInfoWithLabel.run {
            with(touristDestinationInfo) {
                // 필수로 표시 될 관광지 정보
                add(("이용 시기" to useseason ?: "정보 없음") as Pair<String, String>)
                add(("이용 시간" to usetime ?: "정보 없음") as Pair<String, String>)
                add(("개장일" to opendate ?: "정보 없음") as Pair<String, String>)
                add(("쉬는날" to restdate ?: "정보 없음") as Pair<String, String>)
                add(("주차 시설" to parking ?: "정보 없음") as Pair<String, String>)
                // 있으면 표시, 없으면 표시되지 않을 관광지 정보
                accomcount?.let {
                    add("수용 인원" to it)
                }
                chkbabycarriage?.let {
                    add("유모차 대여" to it)
                }
                chkcreditcard?.let {
                    add("신용카드 가능" to it)
                }
                chkpet?.let {
                    add("반려동물 동반 가능" to it)
                }
                expagerange?.let {
                    add("체험 가능 연령" to it)
                }
                expguide?.let {
                    add("체험 안내" to it)
                }
                heritage1?.let {
                    add("세계 문화 유산" to it)
                }
                heritage2?.let {
                    add("세계 자연 유산" to it)
                }
                heritage3?.let {
                    add("세계 기록 유산" to it)
                }
                infocenter?.let {
                    add("문의 및 안내" to it)
                }
            }
        }
        return detailInfoWithLabel.toList()
    }
}