package com.twoday.todaytrip.tourData

import com.twoday.todaytrip.tourApi.AreaBasedListItem
import com.twoday.todaytrip.tourApi.IntroDetailItem

class CulturalFacilities(
    private val _tourItemInfo: AreaBasedListItem,
    private val culturalFacilitiesInfo: IntroDetailItem
) : TourItem(tourItemInfo = _tourItemInfo) {
    /*
    data class CulturalFacilitiesInfo(
        val parking: String? = null,
        val parkingFee: String? = null,
        val useTime: String? = null,
        val restDate: String? = null,
        val accomCount: String? = null,
        val babyCarriage: String? = null,
        val creditCard: String? = null,
        val pet: String? = null,
        val discountInfo: String? = null,
        val infoCenter: String? = null,
        val useFee: String? = null,
        val scale: String? = null,
        val spendTime: String? = null
    )
     */

    override fun getTimeInfoWithLabel(): List<Pair<String, String>> =
        listOf(
            ("이용 시간" to culturalFacilitiesInfo.usetimeculture ?: "정보 없음") as Pair<String, String>,
            ("쉬는날" to culturalFacilitiesInfo.restdateculture ?: "정보 없음") as Pair<String, String>
        )

    override fun getDetailInfoWithLabel(): List<Pair<String, String>> {
        val detailInfoWithLabel = mutableListOf<Pair<String, String>>()
        detailInfoWithLabel.run{
            with(culturalFacilitiesInfo) {
                // 필수로 표시 될 문화시설 정보
                add(("이용 요금" to usefee ?: "정보 없음") as Pair<String, String>)
                add(("이용 시간" to usetimeculture ?: "정보 없음") as Pair<String, String>)
                add(("쉬는날" to restdateculture ?: "정보 없음") as Pair<String, String>)
                add(("주차 시설" to parkingculture ?: "정보 없음") as Pair<String, String>)
                // 있으면 표시, 없으면 표시되지 않을 문화시설 정보
                parkingfee?.let {
                    add("주차 요금" to it)
                }
                accomcountculture?.let {
                    add("수용 인원" to it)
                }
                chkbabycarriageculture?.let {
                    add("유모차 대여" to it)
                }
                chkcreditcardculture?.let {
                    add("신용카드 가능" to it)
                }
                chkpetculture?.let {
                    add("반려동물 동반 가능" to it)
                }
                discountinfo?.let {
                    add("할인 정보" to it)
                }
                scale?.let {
                    add("규모" to it)
                }
                spendtime?.let {
                    add("관람 소요 시간" to it)
                }
                infocenterculture?.let {
                    add("문의 및 안내" to it)
                }
            }
        }
        return detailInfoWithLabel.toList()
    }
}