package app.wowdrobe.com.firebase.firestore

import app.wowdrobe.com.buyClothes.getTimeAgo
import app.wowdrobe.com.tags.TagWithoutTips
import java.util.Locale

data class WasteItem(
    val latitude: Double,
    val longitude: Double,
    val imagePath: String,
    val timeStamp: Long,
    val userEmail: String,
    val address: String,
    val tag: List<TagWithoutTips> = emptyList(),
) {
    constructor() : this(
        0.0,
        0.0,
        "",
        0,
        "",
        "",
        emptyList()
    )

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            address.lowercase(Locale.ROOT),
            tag.joinToString(separator = " ") { it.name.lowercase(Locale.ROOT) },
            getTimeAgo(timeStamp)
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

data class CollectedWasteItem(
    val latitude: Double,
    val longitude: Double,
    val imagePath: String,
    val timeStamp: Long,
    val userEmail: String,
    val address: String,
    val isWasteCollected: Boolean,
    val allWasteCollected: Boolean,
    val feedBack: String,
    val beforeCollectedPath: String? = null
) {
    constructor() : this(
        0.0,
        0.0,
        "",
        0,
        "",
        "",
        false,
        false,
        "",
        ""
    )
}
