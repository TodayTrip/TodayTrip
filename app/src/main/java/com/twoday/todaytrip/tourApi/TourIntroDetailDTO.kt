package com.twoday.todaytrip.tourApi

import com.squareup.moshi.Json

data class IntroDetail(
    val response: IntroDetailResponse
)
data class IntroDetailResponse(
    val header:IntroDetailResponseHeader,
    val body: IntroDetailResponseBody
)
data class IntroDetailResponseHeader(
    val resultCode:String,
    val resultMsg:String
)
data class IntroDetailResponseBody(
    val items: IntroDetailItems,
    val numOfRows:Int,
    val pageNo:Int,
    val totalCount:Int
)
data class IntroDetailItems(
    val item: List<IntroDetailItem>
)
data class IntroDetailItem(
    @Json(name="contentid")
    val contentId:String,
    @Json(name="contenttypeid")
    val contentTypeId:String,
    // TODO change data class property name to camel case
    // 관광지 정보
    val accomcount:String = "",
    val chkbabycarriage:String = "",
    val chkcreditcard:String = "",
    val chkpet:String = "",
    val expagerange:String = "",
    val expguide:String = "",
    val heritage1:String = "",
    val heritage2:String = "",
    val heritage3:String = "",
    val infocenter:String = "",
    val opendate:String = "",
    val parking:String = "",
    val restdate:String = "",
    val useseason:String = "",
    val usetime:String = "",
    // 문화시설 정보
    val accomcountculture:String = "",
    val chkbabycarriageculture:String = "",
    val chkcreditcardculture:String = "",
    val chkpetculture:String = "",
    val discountinfo:String = "",
    val infocenterculture:String = "",
    val parkingculture:String = "",
    val parkingfee:String = "",
    val restdateculture:String = "",
    val usefee:String = "",
    val usetimeculture:String = "",
    val scale:String = "",
    val spendtime:String = "",
    // 행사/공연/축제 정보
    val agelimit:String = "",
    val bookingplace:String = "",
    val discountinfofestival:String = "",
    val eventenddate:String = "",
    val eventhomepage:String = "",
    val eventplace:String = "",
    val eventstartdate:String = "",
    val festivalgrade:String = "",
    val placeinfo:String = "",
    val playtime:String = "",
    val program:String = "",
    val spendtimefestival:String = "",
    val sponsor1:String = "",
    val sponsor1tel:String = "",
    val sponsor2:String = "",
    val sponsor2tel:String = "",
    val subevent:String = "",
    val usetimefestival:String = "", //이용 요금
    // 여행 코스 정보
    val distance:String = "",
    val infocentertourcourse:String = "",
    val schedule:String = "",
    val taketime:String = "",
    val theme:String = "",
    // 레포츠 정보
    val accomcountleports:String = "",
    val chkbabycarriageleports:String = "",
    val chkcreditcardleports:String = "",
    val chkpetleports:String = "",
    val expagerangeleports:String = "",
    val infocenterleports:String = "",
    val openperiod:String = "",
    val parkingfeeleports:String = "",
    val parkingleports:String = "",
    val reservation:String = "",
    val restdateleports:String = "",
    val scaleleports:String = "",
    val usefeeleports:String = "",
    val usetimeleports:String = "",
    // 숙박 정보
    val accomcountlodging:String = "",
    val benikia:String = "",
    val checkintime:String = "",
    val checkouttime:String = "",
    val chkcooking:String = "",
    val foodplace:String = "",
    val goodstay:String = "",
    val hanok:String = "",
    val infocenterlodging:String = "",
    val parkinglodging:String = "",
    val pickup:String = "",
    val roomcount:String = "",
    val reservationlodging:String = "",
    val reservationurl:String = "",
    val roomtype:String = "",
    val scalelodging:String = "",
    val subfacility:String = "",
    val barbecue:String = "",
    val beauty:String = "",
    val beverage:String = "",
    val bicycle:String = "",
    val campfire:String = "",
    val fitness:String = "",
    val karaoke:String = "",
    val publicbath:String = "",
    val publicpc:String = "",
    val sauna:String = "",
    val seminar:String = "",
    val sports:String = "",
    val refundregulation:String = "",
    // 쇼핑 정보
    val chkbabycarriageshopping:String = "",
    val chkcreditcardshopping:String = "",
    val shopping:String = "",
    val chkpetshopping:String = "",
    val culturecenter:String = "",
    val fairday:String = "",
    val infocentershopping:String = "",
    val opendateshopping:String = "",
    val opentime:String = "",
    val parkingshopping:String = "",
    val restdateshopping:String = "",
    val restroom:String = "",
    val saleitem:String = "",
    val saleitemcost:String = "",
    val scaleshopping:String = "",
    val shopguide:String = "",
    // 음식점 정보
    val chkcreditcardfood:String = "",
    val discountinfofood:String = "",
    val firstmenu:String = "",
    val infocenterfood:String = "",
    val kidsfacility:String = "",
    val opendatefood:String = "",
    val opentimefood:String = "",
    val packing:String = "",
    val parkingfood:String = "",
    val reservationfood:String = "",
    val restdatefood:String = "",
    val scalefood:String = "",
    val seat:String = "",
    val smoking:String = "",
    val treatmenu:String = "",
    val lcnsno:String = ""
)  : java.io.Serializable