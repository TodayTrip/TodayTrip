package com.twoday.todaytrip.tourData

class CulturalFacilities(
    override val tourItemInfo: TourItemInfo,
    private val culturalFacilitiesInfo: CulturalFacilitiesInfo
) : TourItem(tourItemInfo) {
    data class CulturalFacilitiesInfo(
        val parking:String?=null,
        val parkingFee:String?=null,
        val useTime:String?=null,
        val restDate:String?=null,
        val accomCount:String?=null,
        val babyCarriage:String?=null,
        val creditCard:String?=null,
        val pet:String?=null,
        val discountInfo:String?=null,
        val infoCenter:String?=null,
        val useFee:String?=null,
        val scale:String?=null,
        val spendTime:String?=null
    )

    override fun getInfoWithLabel(): List<Pair<String, String>> {
        val infoWithLabel = mutableListOf<Pair<String,String>>()
        infoWithLabel.run{
            with(culturalFacilitiesInfo){
                // 필수로 표시 될 문화시설 정보
                add(("이용 요금" to useFee?:"정보 없음") as Pair<String, String>)
                add(("이용 시간" to useTime?:"정보 없음") as Pair<String, String>)
                add(("쉬는날" to restDate?:"정보 없음") as Pair<String, String>)
                add(("주차 시설" to parking?:"정보 없음") as Pair<String, String>)
                // 있으면 표시, 없으면 표시되지 않을 문화시설 정보
                parkingFee?.let{
                    add("주차 요금" to it)
                }
                accomCount?.let{
                    add("수용 인원" to it)
                }
                babyCarriage?.let{
                    add("유모차 대여" to it)
                }
                creditCard?.let{
                    add("신용카드 가능" to it)
                }
                pet?.let{
                    add("반려동물 동반 가능" to it)
                }
                discountInfo?.let{
                    add("할인 정보" to it)
                }
                scale?.let{
                    add("규모" to it)
                }
                spendTime?.let{
                    add("관람 소요 시간" to it)
                }
                infoCenter?.let{
                    add("문의 및 안내" to it)
                }
            }
        }
        return infoWithLabel.toList()
    }
}