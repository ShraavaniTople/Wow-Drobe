package app.wowdrobe.com.ktorClient.repository

import app.wowdrobe.com.ktorClient.Resource
import app.wowdrobe.com.ktorClient.geocoding.GeoCodes
import app.wowdrobe.com.ktorClient.here.dto.HerePlaces
import app.wowdrobe.com.ktorClient.hereSearch.HereSearchResponse
import app.wowdrobe.com.ktorClient.imageFromText.ImageFromText


interface PlacesRepository {
    suspend fun getPlaces(latLong: String): Resource<HerePlaces>

    suspend fun getGeocodingData(query: String): GeoCodes

    suspend fun hereSearch(
        query: String,
        latitude: Double,
        longitude: Double,
        limit: Int = 6,
    ): HereSearchResponse

    suspend fun textToImage(imageFromText: ImageFromText): ByteArray?



}
