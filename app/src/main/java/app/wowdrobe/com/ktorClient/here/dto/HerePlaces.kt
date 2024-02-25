package app.wowdrobe.com.ktorClient.here.dto


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class HerePlaces(
    @SerializedName("items")
    val items: List<Item?>?
)