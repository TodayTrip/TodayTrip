package com.twoday.todaytrip.tourData

import com.twoday.todaytrip.tourApi.AreaBasedListItem
import com.twoday.todaytrip.tourApi.LocationBasedItem

object TourItemDTOConverter{
    fun getAreaBasedFromLocationBased(locationBasedItem: LocationBasedItem): AreaBasedListItem{
        return locationBasedItem.run{
            AreaBasedListItem(
                title = title,
                contentId = contentid,
                contentTypeId = contenttypeid,
                createdTime = createdtime,
                modifiedTime = modifiedtime,
                tel = tel,
                address = addr1,
                addressDetail = addr2,
                zipcode = "",
                mapX = mapx,
                mapY = mapy,
                mapLevel = "",
                areaCode = areacode,
                siGunGuCode = "",
                category1 = cat1,
                category2 = cat2,
                category3 = cat3,
                firstImage = firstimage,
                firstImageThumbnail = firstimage2,
                bookTour = booktour,
                copyrightType = cpyrhtDivCd
            )
        }
    }
}