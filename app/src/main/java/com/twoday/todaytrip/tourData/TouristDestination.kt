package com.twoday.todaytrip.tourData

class TouristDestination(
    override val tourItemInfo: TourItemInfo,
    private val touristDestinationInfo: TouristDestinationInfo
) : TourItem(tourItemInfo) {
    data class TouristDestinationInfo(
        val accomCount: String? = null,
        val chkBabyCarriage: String? = null,
        val chkCreditCard: String? = null,
        val chkPet: String? = null,
        val expAgeRange: String? = null,
        val expGuide: String? = null,
        val heritage1: String? = null,
        val heritage2: String? = null,
        val heritage3: String? = null,
        val infoCenter: String? = null,
        val openDate: String? = null,
        val parking: String? = null,
        val restDate: String? = null,
        val useSeason: String? = null,
        val useTime: String? = null,
    )

    fun getTouristDestinationInfoWithLabel(): List<Pair<String, String>> {
        val infoWithLabel = mutableListOf<Pair<String, String>>()
        infoWithLabel.run {
            with(touristDestinationInfo) {
                // 필수로 표시 될 관광지 정보
                add(("이용 시기" to useSeason ?: "정보 없음") as Pair<String, String>)
                add(("이용 시간" to useTime ?: "정보 없음") as Pair<String, String>)
                add(("개장일" to openDate?: "정보 없음") as Pair<String, String>)
                add(("쉬는날" to restDate ?: "정보 없음") as Pair<String, String>)
                add(("주차시설" to parking ?: "정보 없음") as Pair<String, String>)
                // 있으면 표시, 없으면 표시되지 않을 관광지 정보
                accomCount?.let {
                    add("수용 인원" to it)
                }
                chkBabyCarriage?.let {
                    add("유모차 대여" to it)
                }
                chkCreditCard?.let {
                    add("신용 카드 가능" to it)
                }
                chkPet?.let{
                    add("애완 동물 동반 가능" to it)
                }
                expAgeRange?.let{
                    add("체험 가능 연령" to it)
                }
                expGuide?.let{
                    add("체험 안내" to it)
                }
                heritage1?.let{
                    add("세계 문화 유산" to it)
                }
                heritage2?.let{
                    add("세계 자연 유산" to it)
                }
                heritage3?.let{
                    add("세계 기록 유산" to it)
                }
                infoCenter?.let{
                    add("문의 및 안내" to it)
                }
            }
        }
        return infoWithLabel.toList()
    }
}