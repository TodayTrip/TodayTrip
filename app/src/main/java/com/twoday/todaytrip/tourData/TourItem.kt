package com.twoday.todaytrip.tourData

import android.util.Log
import com.squareup.moshi.JsonClass
import com.twoday.todaytrip.tourApi.AreaBasedListItem
import com.twoday.todaytrip.tourApi.IntroDetailItem
import com.twoday.todaytrip.utils.DateTimeUtil

@JsonClass(generateAdapter = true)
sealed interface TourItem{
    var isAdded: Boolean
    val tourItemInfo: AreaBasedListItem
    fun getTitle() = tourItemInfo.title
    fun getAddress() = tourItemInfo.address
    fun getImage() = tourItemInfo.firstImage
    fun getLongitude() = tourItemInfo.mapX
    fun getLatitude() = tourItemInfo.mapY
    fun getTimeInfoWithLabel(): List<Pair<String, String>>
    fun getDetailInfoWithLabel(): List<Pair<String, String>>
    fun getContentId() = tourItemInfo.contentId
    fun getContentTypeId() = tourItemInfo.contentTypeId

    private fun getInfoWithNoBlank(info: String): String =
        info.ifBlank { "정보 없음" }

    @JsonClass(generateAdapter = true)
    class TouristDestination(
        private val _tourItemInfo: AreaBasedListItem,
        private val touristDestinationInfo: IntroDetailItem
    ) : TourItem {
        override var isAdded = false
        override val tourItemInfo = _tourItemInfo
        override fun getTimeInfoWithLabel(): List<Pair<String, String>> {
            Log.d("TouristDestination", "getTimeInfoWithLabel()")
            return listOf(
                "이용 시간" to super.getInfoWithNoBlank(touristDestinationInfo.usetime),
                "쉬는 날" to super.getInfoWithNoBlank(touristDestinationInfo.restdate)
            )
        }

        override fun getDetailInfoWithLabel(): List<Pair<String, String>> {
            val detailInfoWithLabel = mutableListOf<Pair<String, String>>()
            detailInfoWithLabel.run {
                with(touristDestinationInfo) {
                    // 필수로 표시 될 관광지 정보
                    addAll(getTimeInfoWithLabel())
                    add("개장일" to super.getInfoWithNoBlank(opendate))
                    add("주차 시설" to super.getInfoWithNoBlank(parking))
                    // 있으면 표시, 없으면 표시되지 않을 관광지 정보
                    if (accomcount.isNotBlank()) add("수용 인원" to accomcount)
                    if (chkbabycarriage.isNotBlank()) add("유모차 대여" to chkbabycarriage)
                    if (chkcreditcard.isNotBlank()) add("신용카드 가능 여부" to chkcreditcard)
                    if (chkpet.isNotBlank()) add("반려동물 동반 가능 여부" to chkpet)
                    if (expagerange.isNotBlank()) add("체험 가능 연령" to expagerange)
                    if (expguide.isNotBlank()) add("체험 안내" to expguide)
                    if ((heritage1.isNotBlank())&&(heritage1 != "0")) add("세계 문화 유산" to heritage1)
                    if ((heritage2.isNotBlank())&&(heritage2 != "0")) add("세계 자연 유산" to heritage2)
                    if ((heritage3.isNotBlank())&&(heritage3 != "0")) add("세계 기록 유산" to heritage3)
                    if (infocenter.isNotBlank()) add("문의 및 안내" to infocenter)
                }
            }
            return detailInfoWithLabel.toList()
        }
    }

    @JsonClass(generateAdapter = true)
    class CulturalFacilities(
        private val _tourItemInfo: AreaBasedListItem,
        private val culturalFacilitiesInfo: IntroDetailItem
    ) : TourItem {
        override var isAdded = false
        override val tourItemInfo = _tourItemInfo
        override fun getTimeInfoWithLabel(): List<Pair<String, String>> {
            Log.d("CulturalFacilities", "getTimeInfoWithLabel()")
            return listOf(
                "이용 시간" to super.getInfoWithNoBlank(culturalFacilitiesInfo.usetimeculture),
                "쉬는 날" to super.getInfoWithNoBlank(culturalFacilitiesInfo.restdateculture)
            )
        }

        override fun getDetailInfoWithLabel(): List<Pair<String, String>> {
            val detailInfoWithLabel = mutableListOf<Pair<String, String>>()
            detailInfoWithLabel.run {
                with(culturalFacilitiesInfo) {
                    // 필수로 표시 될 문화시설 정보
                    addAll(getTimeInfoWithLabel())
                    add("이용 요금" to super.getInfoWithNoBlank(usefee))
                    add("주차 시설" to super.getInfoWithNoBlank(parkingculture))
                    // 있으면 표시, 없으면 표시되지 않을 문화시설 정보
                    if (parkingfee.isNotBlank()) add("주차 요금" to parkingfee)
                    if (accomcountculture.isNotBlank()) add("수용 인원" to accomcountculture)
                    if (chkbabycarriageculture.isNotBlank()) add("유모차 대여" to chkbabycarriageculture)
                    if (chkcreditcardculture.isNotBlank()) add("신용카드 가능 여부" to chkcreditcardculture)
                    if (chkpetculture.isNotBlank()) add("반려동물 동반 가능 여부" to chkpetculture)
                    if (discountinfo.isNotBlank()) add("할인 정보" to discountinfo)
                    if (scale.isNotBlank()) add("규모" to scale)
                    if (spendtime.isNotBlank()) add("관람 소요 시간" to spendtime)
                    if (infocenterculture.isNotBlank()) add("문의 및 안내" to infocenterculture)
                }
            }
            return detailInfoWithLabel.toList()
        }
    }

    @JsonClass(generateAdapter = true)
    class Restaurant(
        private val _tourItemInfo: AreaBasedListItem,
        private val restaurantInfo: IntroDetailItem
    ) : TourItem {
        override var isAdded = false
        override val tourItemInfo = _tourItemInfo
        override fun getTimeInfoWithLabel(): List<Pair<String, String>> =
            listOf(
                "이용 시간" to super.getInfoWithNoBlank(restaurantInfo.opentimefood),
                "쉬는 날" to super.getInfoWithNoBlank(restaurantInfo.restdatefood)
            )

        override fun getDetailInfoWithLabel(): List<Pair<String, String>> {
            val infoWithLabel = mutableListOf<Pair<String, String>>()
            infoWithLabel.run {
                with(restaurantInfo) {
                    // 필수로 표시 될 문화시설 정보
                    addAll(getTimeInfoWithLabel())
                    add("주차 시설" to super.getInfoWithNoBlank(parkingfood))
                    add("대표 메뉴" to super.getInfoWithNoBlank(firstmenu))
                    // 있으면 표시, 없으면 표시되지 않을 문화시설 정보
                    if (treatmenu.isNotBlank()) add("취급 메뉴" to treatmenu)
                    if (packing.isNotBlank()) add("포장 가능" to packing)
                    if (chkcreditcardfood.isNotBlank()) add("신용카드 가능 여부" to chkcreditcardfood)
                    if (discountinfofood.isNotBlank()) add("할인 정보" to discountinfofood)
                    if (scalefood.isNotBlank()) add("규모" to scalefood)
                    if (seat.isNotBlank()) add("좌석 수" to seat)
                    if (smoking.isNotBlank()) add("금연/흡연 여부" to smoking)
                    if (kidsfacility.isNotBlank()) add("어린이 놀이방" to kidsfacility)
                    if (opendatefood.isNotBlank()) add("개업일" to opendatefood)
                    if (lcnsno.isNotBlank()) add("인허가번호" to lcnsno)
                    if (infocenterfood.isNotBlank()) add("문의 및 안내" to infocenterfood)
                    if (reservationfood.isNotBlank()) add("예약 안내" to reservationfood)
                }
            }
            return infoWithLabel.toList()
        }
    }

    @JsonClass(generateAdapter = true)
    class LeisureSports(
        private val _tourItemInfo: AreaBasedListItem,
        private val leisureSportsInfo: IntroDetailItem
    ) : TourItem {
        override var isAdded = false
        override val tourItemInfo = _tourItemInfo
        override fun getTimeInfoWithLabel(): List<Pair<String, String>> =
            listOf(
                "이용 시간" to super.getInfoWithNoBlank(leisureSportsInfo.usetimeleports),
                "쉬는 날" to super.getInfoWithNoBlank(leisureSportsInfo.restdateleports)
            )

        override fun getDetailInfoWithLabel(): List<Pair<String, String>> {
            val infoWithLabel = mutableListOf<Pair<String, String>>()
            infoWithLabel.run {
                with(leisureSportsInfo) {
                    // 필수로 표시 될 레포츠 정보
                    addAll(getTimeInfoWithLabel())
                    add("입장료" to super.getInfoWithNoBlank(usefeeleports))
                    add("개장 기간" to super.getInfoWithNoBlank(openperiod))
                    add("주차 시설" to super.getInfoWithNoBlank(parking))
                    // 있으면 표시, 없으면 표시되지 않을 레포츠 정보
                    if (parkingfeeleports.isNotBlank()) add("주차 요금" to parkingfeeleports)
                    if (expagerangeleports.isNotBlank()) add("체험 가능 연령" to expagerangeleports)
                    if (reservation.isNotBlank()) add("예약 안내" to reservation)
                    if (scaleleports.isNotBlank()) add("규모" to scaleleports)
                    if (accomcountleports.isNotBlank()) add("수용 인원" to accomcountleports)
                    if (chkbabycarriageleports.isNotBlank()) add("유모차 대여" to chkbabycarriageleports)
                    if (chkcreditcardleports.isNotBlank()) add("신용카드 가능 여부" to chkcreditcardleports)
                    if (chkpetleports.isNotBlank()) add("반려동물 동반 가능 여부" to chkpetleports)
                    if (infocenterleports.isNotBlank()) add("문의 및 안내" to infocenterleports)
                }
            }
            return infoWithLabel.toList()
        }
    }

    @JsonClass(generateAdapter = true)
    class EventPerformanceFestival(
        private val _tourItemInfo: AreaBasedListItem,
        private val eventPerformanceFestivalInfo: IntroDetailItem
    ) : TourItem {
        override var isAdded = false
        override val tourItemInfo = _tourItemInfo

        fun isEventPerformanceFestivalOver(): Boolean{
            val currentDate = DateTimeUtil.getCurrentDateWithNoLine()
            val endDate = super.getInfoWithNoBlank(eventPerformanceFestivalInfo.eventenddate)
            return (currentDate > endDate)
        }
        override fun getTimeInfoWithLabel(): List<Pair<String, String>> =
            mutableListOf<Pair<String, String>>().apply {
                add("행사 시간" to super.getInfoWithNoBlank(eventPerformanceFestivalInfo.playtime))
                val eventStartDate =
                    super.getInfoWithNoBlank(eventPerformanceFestivalInfo.eventstartdate)
                val eventEndDate =
                    super.getInfoWithNoBlank(eventPerformanceFestivalInfo.eventenddate)
                add("행사 기간" to "${eventStartDate}~${eventEndDate}")
            }.toList()


        override fun getDetailInfoWithLabel(): List<Pair<String, String>> {
            val infoWithLabel = mutableListOf<Pair<String, String>>()
            infoWithLabel.run {
                with(eventPerformanceFestivalInfo) {
                    // 필수로 표시 될 행사/공연/축제 정보
                    addAll(getTimeInfoWithLabel())
                    add(("이용 요금" to (usefee ?: "정보 없음")) as Pair<String, String>)
                    // 있으면 표시, 없으면 표시되지 않을 행사/공연/축제 정보
                    if (agelimit.isNotBlank()) add("관람 가능 연령" to agelimit)
                    if (bookingplace.isNotBlank()) add("예매처" to bookingplace)
                    if (discountinfofestival.isNotBlank()) add("할인 정보" to discountinfofestival)
                    if (eventstartdate.isNotBlank()) add("행사 시작일" to eventstartdate)
                    if (eventenddate.isNotBlank()) add("행사 종료일" to eventenddate)
                    if (eventplace.isNotBlank()) add("행사 장소" to eventplace)
                    if (placeinfo.isNotBlank()) add("행사장 위치 안내" to placeinfo)
                    if (eventhomepage.isNotBlank()) add("행사 홈페이지" to eventhomepage)
                    if (program.isNotBlank()) add("행사 프로그램" to program)
                    if (subevent.isNotBlank()) add("부대 행사" to subevent)
                    if (festivalgrade.isNotBlank()) add("축제 등급" to festivalgrade)
                    if (playtime.isNotBlank()) add("공연 시간" to playtime)
                    if (spendtimefestival.isNotBlank()) add("관람 소요 시간" to spendtimefestival)
                    if (sponsor1.isNotBlank()) add("주최자 정보" to sponsor1)
                    if (sponsor1tel.isNotBlank()) add("주최자 연락처" to sponsor1tel)
                    if (sponsor2.isNotBlank()) add("주관사 정보" to sponsor2)
                    if (sponsor2tel.isNotBlank()) add("주관사 연락처" to sponsor2tel)
                }
            }
            return infoWithLabel.toList()
        }
    }
}
