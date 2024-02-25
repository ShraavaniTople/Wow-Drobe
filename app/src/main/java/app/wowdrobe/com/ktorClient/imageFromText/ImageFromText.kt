package app.wowdrobe.com.ktorClient.imageFromText


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ImageFromText(
    @SerializedName("inputs")
    val inputs: String?
)