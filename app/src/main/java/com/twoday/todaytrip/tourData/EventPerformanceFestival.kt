package com.twoday.todaytrip.tourData

class EventPerformanceFestival(
    override val tourItemInfo: TourItemInfo,
    private val eventPerformanceFestivalInfo: EventPerformanceFestivalInfo
) : TourItem(tourItemInfo) {
    data class EventPerformanceFestivalInfo(
        val ageLimit: String? = null,
        val bookingPlace: String? = null,
        val discountInfo: String? = null,
        val eventEndDate: String? = null,
        val eventHomepage: String? = null,
        val eventPlace: String? = null,
        val eventStartDate: String? = null,
        val festivalGrade: String? = null,
        val placeInfo: String? = null,
        val playTime: String? = null,
        val program: String? = null,
        val spendTime: String? = null,
        val sponsor1: String? = null,
        val sponsor1Tel: String? = null,
        val sponsor2: String? = null,
        val sponsor2Tel: String? = null,
        val subEvent: String? = null,
        val useFee: String? = null
    )

    override fun getInfoWithLabel(): List<Pair<String, String>> {
        val infoWithLabel = mutableListOf<Pair<String, String>>()
        infoWithLabel.run {
            with(eventPerformanceFestivalInfo) {
                // 필수로 표시 될 행사/공연/축제 정보
                add(("이용 요금" to useFee ?: "정보 없음") as Pair<String, String>)
                // 있으면 표시, 없으면 표시되지 않을 행사/공연/축제 정보
                ageLimit?.let {
                    add("관람 가능 연령" to it)
                }
                bookingPlace?.let {
                    add("예매처" to it)
                }
                discountInfo?.let {
                    add("할인 정보" to it)
                }
                eventStartDate?.let {
                    add("행사 시작일" to it)
                }
                eventEndDate?.let {
                    add("행사 종료일" to it)
                }
                eventPlace?.let {
                    add("행사 장소" to it)
                }
                placeInfo?.let {
                    add("행사장 위치 안내" to it)
                }
                eventHomepage?.let {
                    add("행사 홈페이지" to it)
                }
                program?.let {
                    add("행사 프로그램" to it)
                }
                subEvent?.let {
                    add("부대 행사" to it)
                }
                festivalGrade?.let {
                    add("축제 등급" to it)
                }
                playTime?.let {
                    add("공연 시간" to it)
                }
                spendTime?.let {
                    add("관람 소요 시간" to it)
                }
                sponsor1?.let {
                    add("주최자 정보" to it)
                }
                sponsor1Tel?.let {
                    add("추죄자 연락처" to it)
                }
                sponsor2?.let {
                    add("주관사 정보" to it)
                }
                sponsor2Tel?.let {
                    add("주관사 연락처" to it)
                }
            }
        }
        return infoWithLabel.toList()
    }
}