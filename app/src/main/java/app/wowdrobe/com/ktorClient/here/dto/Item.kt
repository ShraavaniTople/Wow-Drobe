package app.wowdrobe.com.ktorClient.here.dto


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class Item(
    @SerializedName("access")
    val access: List<Acces?>?,
    @SerializedName("address")
    val address: Address?,
    @SerializedName("categories")
    val categories: List<Category?>?,
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("position")
    val position: Position?,
    @SerializedName("resultType")
    val resultType: String?,
    @SerializedName("title")
    val title: String?
)