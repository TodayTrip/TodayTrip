package com.twoday.todaytrip.tourData

class Restaurant(
    override val tourItemInfo: TourItemInfo,
    private val restaurantInfo: RestaurantInfo
):TourItem(tourItemInfo) {
    data class RestaurantInfo(
        val creditCard:String?=null,
        val discountInfo:String?=null,
        val firstMenu:String?=null,
        val infoCenter:String?=null,
        val kidsFacility:String?=null,
        val openDate:String?=null,
        val openTime:String?=null,
        val packing:String?=null,
        val parking:String?=null,
        val reservation:String?=null,
        val restDate:String?=null,
        val scale:String?=null,
        val seat:String?=null,
        val smoking:String?=null,
        val treatMenu:String?=null,
        val lcnsNo:String?=null
    )

    override fun getInfoWithLabel(): List<Pair<String, String>> {
        val infoWithLabel = mutableListOf<Pair<String,String>>()
        infoWithLabel.run{
            with(restaurantInfo){
                // 필수로 표시 될 문화시설 정보
                add(("영업 시간" to openTime?:"정보 없음")as Pair<String, String>)
                add(("쉬는날" to restDate?:"정보 없음")as Pair<String, String>)
                add(("주차 시설" to packing?:"정보 없음")as Pair<String, String>)
                add(("대표 메뉴" to firstMenu?:"정보 없음")as Pair<String, String>)
                // 있으면 표시, 없으면 표시되지 않을 문화시설 정보
                treatMenu?.let{
                    add("취급 메뉴" to it)
                }
                packing?.let {
                    add("포장 가능" to it)
                }
                creditCard?.let{
                    add("신용카드 가능" to it)
                }
                discountInfo?.let{
                    add("할인 정보" to it)
                }
                scale?.let{
                    add("규모" to it)
                }
                seat?.let{
                    add("좌석 수" to it)
                }
                smoking?.let{
                    add("금연/흡연 여부" to it)
                }
                kidsFacility?.let{
                    add("어린이 놀이방" to it)
                }
                openDate?.let{
                    add("개업일" to it)
                }
                lcnsNo?.let{
                    add("인허가번호" to it)
                }
                infoCenter?.let{
                    add("문의 및 안내" to it)
                }
                reservation?.let{
                    add("예약 안내" to it)
                }
            }
        }
        return infoWithLabel.toList()
    }
}