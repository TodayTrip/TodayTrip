package com.twoday.todaytrip.tourData

import com.twoday.todaytrip.tourApi.AreaBasedListItem
import com.twoday.todaytrip.tourApi.IntroDetailItem

class EventPerformanceFestival(
    private val _tourItemInfo: AreaBasedListItem,
    private val eventPerformanceFestivalInfo: IntroDetailItem
) : TourItem(tourItemInfo = _tourItemInfo) {
    override fun getTimeInfoWithLabel(): List<Pair<String, String>> =
        mutableListOf<Pair<String, String>>().apply {
            eventPerformanceFestivalInfo.eventstartdate?.let {
                add("행사 시작일" to it)
            }
            eventPerformanceFestivalInfo.eventenddate?.let {
                add("행사 종료일" to it)
            }
            eventPerformanceFestivalInfo.playtime?.let {
                add("공연 시간" to it)
            }
        }.toList()


    override fun getDetailInfoWithLabel(): List<Pair<String, String>> {
        val infoWithLabel = mutableListOf<Pair<String, String>>()
        infoWithLabel.run {
            with(eventPerformanceFestivalInfo) {
                // 필수로 표시 될 행사/공연/축제 정보
                add(("이용 요금" to usefee ?: "정보 없음") as Pair<String, String>)
                // 있으면 표시, 없으면 표시되지 않을 행사/공연/축제 정보
                agelimit?.let {
                    add("관람 가능 연령" to it)
                }
                bookingplace?.let {
                    add("예매처" to it)
                }
                discountinfofestival?.let {
                    add("할인 정보" to it)
                }
                eventstartdate?.let {
                    add("행사 시작일" to it)
                }
                eventenddate?.let {
                    add("행사 종료일" to it)
                }
                eventplace?.let {
                    add("행사 장소" to it)
                }
                placeinfo?.let {
                    add("행사장 위치 안내" to it)
                }
                eventhomepage?.let {
                    add("행사 홈페이지" to it)
                }
                program?.let {
                    add("행사 프로그램" to it)
                }
                subevent?.let {
                    add("부대 행사" to it)
                }
                festivalgrade?.let {
                    add("축제 등급" to it)
                }
                playtime?.let {
                    add("공연 시간" to it)
                }
                spendtimefestival?.let {
                    add("관람 소요 시간" to it)
                }
                sponsor1?.let {
                    add("주최자 정보" to it)
                }
                sponsor1tel?.let {
                    add("추죄자 연락처" to it)
                }
                sponsor2?.let {
                    add("주관사 정보" to it)
                }
                sponsor2tel?.let {
                    add("주관사 연락처" to it)
                }
            }
        }
        return infoWithLabel.toList()
    }
}