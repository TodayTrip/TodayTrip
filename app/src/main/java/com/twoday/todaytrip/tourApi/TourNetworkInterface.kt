package com.twoday.todaytrip.tourApi

import com.twoday.todaytrip.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

const val MOBILE_OS = "AND"
const val MOBILE_NAME = "TodayTrip"
const val TOUR_API_KEY: String = BuildConfig.TOUR_API_KEY

interface TourNetworkInterface {
    //지역 기반 관광 정보 조회
    @GET("areaBasedList1")
    suspend fun getAreaBasedList(
        // 필수 파라미터
        @Query("serviceKey") serviceKey: String = TOUR_API_KEY,
        @Query("MobileOS") os: String = MOBILE_OS,
        @Query("MobileApp") appName: String = MOBILE_NAME,
        // 선택 파라미터
        @Query("contentTypeId") contentTypeId: String? = null, //관광 타입
        @Query("areaCode") areaCode: String? = null, //지역 코드
        @Query("sigunguCode") siGunGuCode: String? = null, //시군구 코드

        @Query("cat1") category1: String? = null, //카테고리 대분류
        @Query("cat2") category2: String? = null, //카테고리 중분류
        @Query("cat3") category3: String? = null, //카테고리 소분류

        @Query("numOfRows") numOfRows: Int? = null, // 한 페이지 결과 수
        @Query("pageNo") pageNo: Int? = null, // 페이지 번호
        @Query("_type") responseType: String = "json", //응답 타입 (json, xml)

        @Query("listYN") isList: String = "Y", // 목록 구분(Y=목록, N=개수)
        @Query("arrange") sort: String = "A"
        // 정렬 구분(A=제목 순, C=수정 순, D=생성일 순)
        // 대표 이미지가 필수인 정렬 구분(O=제목 순, Q=수정 순, R=생성일 순)
    ): AreaBasedList

    //공통 정보 조회
    @GET("detailCommon1")
    suspend fun getCommonDetail(
        //필수 파라미터
        @Query("contentId") contentId: String, //공통 정보를 조회할 장소의 ID

        @Query("serviceKey") serviceKey: String = TOUR_API_KEY,
        @Query("MobileOS") os: String = MOBILE_OS,
        @Query("MobileApp") appName: String = MOBILE_NAME,
        //선택 파라미터
        @Query("contentTypeId") contentTypeId: String? = null,

        @Query("defaultYN") defaultYN: String? = "Y",
        @Query("firstImageYN") firstImageYN: String? = "Y",
        @Query("areacodeYN") areaCodeYN: String? = "Y",
        @Query("catcodeYN") categoryCodeYN: String? = "Y",
        @Query("addrinfoYN") addressInfoYN: String? = "Y",
        @Query("mapinfoYN") mapInfoYN: String? = "Y",
        @Query("overviewYN") overviewYN: String? = "Y",

        @Query("numOfRows") numOfRows: Int? = null,
        @Query("pageNo") pageNo: Int? = null,
        @Query("_type") responseType: String = "json"
    ): CommonDetail

    //소개 정보 조회
    @GET("detailIntro1")
    suspend fun getIntroDetail(
        //필수 파라미터
        @Query("contentId") contentId: String, // 공통 정보를 조회할 장소의 ID
        @Query("contentTypeId") contentTypeId: String, // 공통 정보를 조회할 장소의 컨텐츠 타입 ID

        @Query("serviceKey") serviceKey: String = TOUR_API_KEY,
        @Query("MobileOS") os: String = MOBILE_OS,
        @Query("MobileApp") appName: String = MOBILE_NAME,
        //선택 파라미터
        @Query("numOfRows") numOfRows: Int? = null,
        @Query("pageNo") pageNo:Int? = null,
        @Query("_type") responseType:String = "json"
    ):IntroDetail
}