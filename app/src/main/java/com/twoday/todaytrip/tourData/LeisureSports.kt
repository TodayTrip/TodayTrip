package com.twoday.todaytrip.tourData

import com.twoday.todaytrip.tourApi.AreaBasedListItem
import com.twoday.todaytrip.tourApi.IntroDetailItem

class LeisureSports(
    private val _tourItemInfo: AreaBasedListItem,
    private val leisureSportsInfo: IntroDetailItem
) : TourItem(tourItemInfo = _tourItemInfo) {
    override fun getTimeInfoWithLabel(): List<Pair<String, String>> =
        listOf(
            ("이용 시간" to leisureSportsInfo.usetimeleports) as Pair<String, String>,
            ("쉬는 날" to leisureSportsInfo.restdateleports) as Pair<String, String>
        )

    override fun getDetailInfoWithLabel(): List<Pair<String, String>> {
        val infoWithLabel = mutableListOf<Pair<String, String>>()
        infoWithLabel.run {
            with(leisureSportsInfo) {
                // 필수로 표시 될 레포츠 정보
                add(("입장료" to usefeeleports) as Pair<String, String>)
                add(("개장 기간" to openperiod) as Pair<String, String>)
                add(("이용 시간" to usetimeleports) as Pair<String, String>)
                add(("쉬는 날" to restdateleports) as Pair<String, String>)
                add(("주차 시설" to parking) as Pair<String, String>)
                // 있으면 표시, 없으면 표시되지 않을 레포츠 정보
                parkingfeeleports?.let {
                    add("주차 요금" to it)
                }
                expagerangeleports?.let {
                    add("체험 가능 연령" to it)
                }
                reservation?.let {
                    add("예약 안내" to it)
                }
                scaleleports?.let {
                    add("규모" to it)
                }
                accomcountleports?.let {
                    add("수용 인원" to it)
                }
                chkbabycarriageleports?.let {
                    add("유모차 대여" to it)
                }
                chkcreditcardleports?.let {
                    add("신용카드 가능" to it)
                }
                chkpetleports?.let {
                    add("반여동물 동반 가능" to it)
                }
                infocenterleports?.let {
                    add("문의 및 안내" to it)
                }
            }
        }
        return infoWithLabel.toList()
    }
}