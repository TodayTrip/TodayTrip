package com.twoday.todaytrip.tourData

class LeisureSports(
    override val tourItemInfo: TourItemInfo,
    private val leisureSportsInfo: LeisureSportsInfo
) : TourItem(tourItemInfo) {
    data class LeisureSportsInfo(
        val accomCount: String?=null,
        val babyCarriage: String?=null,
        val creditCard: String?=null,
        val pet: String?=null,
        val expAgeRange: String?=null,
        val infoCenter: String?=null,
        val openPeriod: String?=null,
        val parkingFee: String?=null,
        val parking: String?=null,
        val reservation: String?=null,
        val restDate: String?=null,
        val scale: String?=null,
        val useFee: String?=null,
        val useTime: String?=null
    )

    override fun getInfoWithLabel(): List<Pair<String, String>> {
        val infoWithLabel = mutableListOf<Pair<String, String>>()
        infoWithLabel.run {
            with(leisureSportsInfo) {
                // 필수로 표시 될 레포츠 정보
                add(("입장료" to useFee) as Pair<String, String>)
                add(("개장 기간" to openPeriod) as Pair<String, String>)
                add(("이용 시간" to useTime) as Pair<String, String>)
                add(("쉬는 날" to restDate) as Pair<String, String>)
                add(("주차 시설" to parking) as Pair<String, String>)
                // 있으면 표시, 없으면 표시되지 않을 레포츠 정보
                parkingFee?.let {
                    add("주차 요금" to it)
                }
                expAgeRange?.let {
                    add("체험 가능 연령" to it)
                }
                reservation?.let{
                    add("예약 안내" to it)
                }
                scale?.let {
                    add("규모" to it)
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
                pet?.let {
                    add("반여동물 동반 가능" to it)
                }
                infoCenter?.let{
                    add("문의 및 안내" to it)
                }
            }
        }
        return infoWithLabel.toList()
    }
}