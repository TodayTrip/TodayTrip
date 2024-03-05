package com.twoday.todaytrip.tourData

import android.util.Log
import com.twoday.todaytrip.tourApi.AreaBasedListItem
import com.twoday.todaytrip.tourApi.IntroDetailItem
import kotlinx.serialization.Serializable

@Serializable
sealed interface TourItem{
    var isAdded: Boolean
    val tourItemInfo: AreaBasedListItem
    fun getTitle() = tourItemInfo.title
    fun getAddress() = tourItemInfo.address ?: "주소 정보 없음"
    fun getThumbnailImage() = tourItemInfo.firstImageThumbnail?: null
    fun getLongitude() = tourItemInfo.mapX
    fun getLatitude() = tourItemInfo.mapY
    abstract fun getTimeInfoWithLabel(): List<Pair<String, String>>
    abstract fun getDetailInfoWithLabel(): List<Pair<String, String>>
    fun getContentId() = tourItemInfo.contentId
    fun getContentTypeId() = tourItemInfo.contentTypeId

    @Serializable
    class TouristDestination(
        private val _tourItemInfo: AreaBasedListItem,
        private val touristDestinationInfo: IntroDetailItem
    ) : TourItem{
        override var isAdded = false
        override val tourItemInfo = _tourItemInfo
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

    @Serializable
    class CulturalFacilities(
        private val _tourItemInfo: AreaBasedListItem,
        private val culturalFacilitiesInfo: IntroDetailItem
    ) : TourItem{
        override var isAdded = false
        override val tourItemInfo = _tourItemInfo
        override fun getTimeInfoWithLabel(): List<Pair<String, String>>{
            Log.d("CulturalFacilities","getTimeInfoWithLabel()")
            return listOf(
                ("이용 시간" to culturalFacilitiesInfo.usetimeculture ?: "정보 없음") as Pair<String, String>,
                ("쉬는날" to culturalFacilitiesInfo.restdateculture ?: "정보 없음") as Pair<String, String>
            )
        }

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

    @Serializable
    class Restaurant(
        private val _tourItemInfo: AreaBasedListItem,
        private val restaurantInfo: IntroDetailItem
    ) : TourItem{
        override var isAdded = false
        override val tourItemInfo = _tourItemInfo
        override fun getTimeInfoWithLabel(): List<Pair<String, String>> =
            listOf(
                ("영업 시간" to restaurantInfo.opentimefood ?: "정보 없음") as Pair<String, String>,
                ("쉬는날" to restaurantInfo.restdatefood ?: "정보 없음") as Pair<String, String>
            )

        override fun getDetailInfoWithLabel(): List<Pair<String, String>> {
            val infoWithLabel = mutableListOf<Pair<String, String>>()
            infoWithLabel.run {
                with(restaurantInfo) {
                    // 필수로 표시 될 문화시설 정보
                    add(("영업 시간" to opentimefood ?: "정보 없음") as Pair<String, String>)
                    add(("쉬는날" to restdatefood ?: "정보 없음") as Pair<String, String>)
                    add(("주차 시설" to parkingfood ?: "정보 없음") as Pair<String, String>)
                    add(("대표 메뉴" to firstmenu ?: "정보 없음") as Pair<String, String>)
                    // 있으면 표시, 없으면 표시되지 않을 문화시설 정보
                    treatmenu?.let {
                        add("취급 메뉴" to it)
                    }
                    packing?.let {
                        add("포장 가능" to it)
                    }
                    chkcreditcardfood?.let {
                        add("신용카드 가능" to it)
                    }
                    discountinfofood?.let {
                        add("할인 정보" to it)
                    }
                    scalefood?.let {
                        add("규모" to it)
                    }
                    seat?.let {
                        add("좌석 수" to it)
                    }
                    smoking?.let {
                        add("금연/흡연 여부" to it)
                    }
                    kidsfacility?.let {
                        add("어린이 놀이방" to it)
                    }
                    opendatefood?.let {
                        add("개업일" to it)
                    }
                    lcnsno?.let {
                        add("인허가번호" to it)
                    }
                    infocenterfood?.let {
                        add("문의 및 안내" to it)
                    }
                    reservationfood?.let {
                        add("예약 안내" to it)
                    }
                }
            }
            return infoWithLabel.toList()
        }
    }
    @Serializable
    class LeisureSports(
        private val _tourItemInfo: AreaBasedListItem,
        private val leisureSportsInfo: IntroDetailItem
    ) : TourItem{
        override var isAdded = false
        override val tourItemInfo = _tourItemInfo
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
    @Serializable
    class EventPerformanceFestival(
        private val _tourItemInfo: AreaBasedListItem,
        private val eventPerformanceFestivalInfo: IntroDetailItem
    ) : TourItem{
        override var isAdded = false
        override val tourItemInfo = _tourItemInfo
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
}
