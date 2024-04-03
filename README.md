## [오늘여행] 무계획 여행자들을 위한 여행지 추천 및 관광 정보 제공 서비스

![img](https://github.com/TodayTrip/TodayTrip/assets/151485887/42e4fab8-be36-415a-8b65-a01267a82ac8)


<a href="https://github.com/TodayTrip/TodayTrip/actions/new">
      <img alt="Tests Passing" src="https://img.shields.io/github/languages/top/TodayTrip/TodayTrip?style=flat&logo=kotlin&logoColor=white&color=800080" />
</a>
<a href="https://github.com/TodayTrip/TodayTrip/commits/dev/">
      <img alt="Tests Coverage" src="https://img.shields.io/github/commit-activity/m/TodayTrip/TodayTrip" />
</a>
<a href="https://github.com/TodayTrip/TodayTrip">
      <img alt="GitHub Contributors" src="https://img.shields.io/github/contributors/TodayTrip/TodayTrip" />
</a>
<a href="https://github.com/TodayTrip/TodayTrip/pulls">
      <img alt="GitHub pull requests" src="https://img.shields.io/github/issues-pr/TodayTrip/TodayTrip?color=red" />
</a>

[App Download](https://play.google.com/store/apps/details?id=com.twoday.todaytrip)

### 👩‍💻팀원 소개🧑‍💻
|Name|Github Link|
|------|---|
|김나희|🔗[kimnahee1529](https://github.com/kimnahee1529)|
|박재원|🔗[parkjaewons](https://github.com/parkjaewons)|
|이선주|🔗[sunjoolee](https://github.com/sunjoolee)|
|송동철|🔗[wade316](https://github.com/wade316)|
|최영정|🔗[YoungjeongChoi](https://github.com/YoungjeongChoi)|
----

### 🧳서비스 소개🧳
<aside>
“오늘여행”은 무계획 여행자들을 위한 여행지 추천 및 관광 정보 제공 서비스입니다.
      
</aside>


<aside> 
      
“오늘여행”은 다음과 같은 분들을 위해 탄생했습니다!

🤩 모험을 즐기며, 즉흥적인 여행을 좋아하시는 분

🤔 여행을 가고 싶지만, 여행 계획을 세우는 것이 부담스러우신 분

🧐 관광 정보를 편하게 찾아보고 싶은 분

😁 여행지 사이의 거리를 한 눈에 보면서 코스를 짜고 싶은 분

</aside>
----

### 🛠기술🛠

#### ⚙기술 스택⚙
|제목|내용|
|------|---|
|Jetpack|`ViewModel` `LiveData` `LifeCycles` `ViewBinding` `AAC` `Navigation`|
|비동기 처리|`Coroutine` `async`|
|데이터 처리|`Percelize` `SharedPreferences` `Serializable` `Gson`|
|API 통신|`Retrofit` `OkHttp`|
|활용 API|`Naver Map API` `Tour API` `기상청 단기 예보 API`|
|이미지 처리|`Glide` `FishBun`|
|UI Frameworks|`Fragment` `RecyclerViewAdapter` `ListAdapter` `Shimmer` `BottomSheet` `Ballon` `ViewPager2` `MapView` `SwipteRefreshLayout` `Word Cloud` `Icon-Switch` `CoordinatorLayout` `MotionLayout` `MeterialDesign` `TedClustering`|  
#### 🔧아키텍처🔧
`MVVM`  
----

### 📱화면📱

## 랜덤

- 여행지 랜덤 선택 기능 제공
    - 사용자가 여행할 지역을 랜덤으로 선택해주는 기능을 제공합니다.
    - 사용자는 (**1) 여행 테마**와, (**2) 랜덤 선택에 포함될 지역**을 선택할 수 있습니다.
- (1) 여행 테마 선택
    - 완전 랜덤 선택 시, 랜덤으로 선택된 여행지의 모든 관광지 정보가 제공됩니다.
    - 테마 별 랜덤 선택 시, 랜덤으로 선택된 여행지의 관광지 정보 중 테마에 맞는 정보만 필터링되어 제공됩니다.
        - 테마 별 제공되는 관광지 정보
            - 풀내음이 가득한 테마: 산, 수목원, 자연 휴양림 관광지 정보 제공
            - 청량함이 있는 테마: 해안절경, 해수욕, 섬, 등대, 항구 관광지 정보 제공
            - 시간을 넘나드는 테마: 역사 관광지 정보 제공
            - 근심이 없어지는 테마: 휴양 관광지 정보 제공
            - 감각을 자극하는 테마: 체험 관광지 정보 제공
            - 활력이 넘치는 테마: 레포츠 관광지 정보 제공
            - 예술이 숨쉬는 테마: 미술관, 전시관, 박물관, 컨벤션 센터, 기념관 정보 제공
- (2) 지역 선택
    - 여행 테마 선택 후, 랜덤 선택에 포함될 지역을 선택할 수 있습니다.
    - 지역 선택 후, 랜덤으로 여행지가 선택되며, **홈**으로 이동합니다.



<img src="https://github.com/TodayTrip/TodayTrip/assets/151485887/4bb2a729-ce57-44e1-b405-5e0994fd774a" width="200"/>
<img src="https://github.com/TodayTrip/TodayTrip/assets/151485887/af80a6d7-272f-4834-a9e4-968c348fd183" width="200"/>
<img src="https://github.com/TodayTrip/TodayTrip/assets/151485887/b36a5bb0-9251-4149-b45d-b8e17d489710" width="200"/>

(0) 시작화면
(1) 완전 랜덤 선택
(2-1) 지역 선택




<img src="https://github.com/TodayTrip/TodayTrip/assets/151485887/de623c9f-f46a-4eb1-8a43-25265e9490ea" width="200"/>
<img src="https://github.com/TodayTrip/TodayTrip/assets/151485887/99f54ac1-2779-4abb-8ef3-fe5be2f56c51" width="200"/>
<img src="https://github.com/TodayTrip/TodayTrip/assets/151485887/6e090f91-6524-4bc8-83b0-d2c5849babc8" width="200"/>

(1-1) 테마별 랜덤 선택
(1-2) 여행 테마 선택 
(2-1) 지역 선택



<img src="https://github.com/TodayTrip/TodayTrip/assets/151485887/3584c371-2042-46d8-b2e9-bc95a0118880" width="200"/>
<img src="https://github.com/TodayTrip/TodayTrip/assets/151485887/2478c77b-02d3-4ff4-82cf-1fdc7d0b1c02" width="200"/>

(2-2) 여러 지역 선택 시, 로딩 화면
(2-2) 하나의 지역 선택 시, 로딩 화면




