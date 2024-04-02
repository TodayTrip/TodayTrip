## [오늘여행] 무계획 여행자들을 위한 여행지 추천 및 관광 정보 제공 서비스

![image](https://github.com/TodayTrip/TodayTrip/assets/86330886/fd7098f2-f0b8-49a4-b9df-695cb45dede3)

<a href="https://github.com/SeeSeeCallCall/SeeSeeCallCall/actions/new">
      <img alt="Tests Passing" src="https://img.shields.io/github/languages/top/SeeSeeCallCall/SeeSeeCallCall?style=flat&logo=kotlin&logoColor=white&color=800080" />
</a>
<a href="https://github.com/SeeSeeCallCall/SeeSeeCallCall/commits/dev/">
      <img alt="Tests Coverage" src="https://img.shields.io/github/commit-activity/m/SeeSeeCallCall/SeeSeeCallCall" />
</a>
<a href="https://github.com/SeeSeeCallCall/SeeSeeCallCall">
      <img alt="GitHub Contributors" src="https://img.shields.io/github/contributors/SeeSeeCallCall/SeeSeeCallCall" />
</a>
<a href="https://github.com/SeeSeeCallCall/SeeSeeCallCall/pulls">
      <img alt="GitHub pull requests" src="https://img.shields.io/github/issues-pr/SeeSeeCallCall/SeeSeeCallCall?color=red" />
</a>

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

“오늘여행”은 다음과 같은 분들을 위해 탄생했습니다!

🤩 모험을 즐기며, 즉흥적인 여행을 좋아하시는 분

🤔 여행을 가고 싶지만, 여행 계획을 세우는 것이 부담스러우신 분

🧐 관광 정보를 편하게 찾아보고 싶은 분

😁 여행지 사이의 거리를 한 눈에 보면서 코스를 짜고 싶은 분

----

### 🛠기술🛠

#### ⚙기술 스택⚙
#### 🔧아키텍처🔧  

----

### 📱화면📱

<details>
<summary>🎲랜덤 선택</summary>
<div markdown="1">

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

|<img src="https://github.com/TodayTrip/TodayTrip/assets/86330886/7cbd470e-d22c-44af-87be-3628f7c91f3f" width="600"/>|<img src="https://github.com/TodayTrip/TodayTrip/assets/86330886/e2b276ce-43c3-42a7-88d2-f33f991fc10a" width="600"/>|<img src="https://github.com/TodayTrip/TodayTrip/assets/86330886/60c58d06-7340-4c8f-8824-b28bb3d7c098" width="600"/>
|--|--|--|

</div>
</details>

<details>
<summary>📜정보 탭</summary>
<div markdown="1">

- 선택한 여행 테마 및 선택된 여행지 이름 표시
    - 화면 상단에 사용자가 선택한 여행 테마 및 랜덤으로 선택된 여행지의 이름이 표시됩니다.
        - 여행 태마를 선택하지 않았을 때, 여행지가 제주로 선택된 경우 : “가고 싶었던” 제주
        - “풀내음이 가득한” 테마를 선택했을 때, 여행지가 제주로 선택된 경우 : “풀내음이 가득한” 제주
- 선택된 여행지의 관광지, 식당, 카페, 행사/축제 정보 제공
    - 랜덤으로 정해진 여행지의 관광지, 식당, 카페, 행사/축제 정보를 Tour API를 이용해 받아와 사용자에게 제공합니다.
    - 각 카테고리(관광지, 식당, 카페, 행사/축제) 별로 각 탭에 장소 정보 목록을 제공합니다.
    - 목록을 통해 장소들의 이름, 썸네일 이미지, 주소, 운영 시간 등 간략한 정보를 확인할 수 있습니다.
    - 경로에 추가된 장소는 목록의 상단으로 이동하고 북마크 아이콘이 생겨서 담은 장소인지 아닌지 간편하게 확인이 가능합니다.
- 랜덤 여행 코스 추천
    - 여행 계획을 보다 쉽게 짤 수 있도록, 여행지 뿐만 아니라 여행 코스를 랜덤으로 선택하여 제공합니다.
        - 여행지에서 방문하면 좋을 장소를 각 카테고리(관광지, 식당, 카페, 행사/축제) 별로 하나 씩 랜덤으로 선택합니다.
        - 추천 코스의 장소들을 최단 거리로 여행할 수 있도록 방문 순서가 정해집니다.
    - 여행 코스를 지도로 한 눈에 확인 가능하고, 한꺼번에 경로에 담아 **경로 탭**에서 확인할 수 있습니다.
    - 추천된 여행 코스가 마음에 안 드는 경우, 새로고침 버튼을 클릭하여 새로운 여행 코스를 추천 받을 수 있습니다.
    - 추천 코스가 표시되는 배너는 자동으로 스크롤이 넘어가, 더 편하게 확인할 수 있습니다.
- 여행지 날씨 정보 제공
    - 선택된 여행지의 위도&경도를 기반으로, 기상청 단기 예보 API를 이용해 받아온 날씨 정보를 사용자에게 제공합니다.
- 여행지 새롭게 랜덤 선택
    - 랜덤 선택된 여행지가 마음에 안 드는 경우, 하단의 중앙의 주사위 아이콘 버튼을 클릭해 여행지를 새롭게 랜덤 선택할 수 있습니다.
    - 여행지를 새로 랜덤 선택 하는 경우, 경로에 담긴 이전 여행지의 장소 목록은 초기화됩니다.

|<img src="https://github.com/TodayTrip/TodayTrip/assets/86330886/abdb02d3-bf64-43f0-9fb8-cbce1dc89ec4" width="600"/>| <img src="https://github.com/TodayTrip/TodayTrip/assets/86330886/449d8752-266f-43c8-8350-40250b07e066" width="600"/> |<img src="https://github.com/TodayTrip/TodayTrip/assets/86330886/13ffef57-5ab5-4c8c-b142-79fbf6bf21dd" width="600"/>|
|--|--|--|

</div>
</details>





