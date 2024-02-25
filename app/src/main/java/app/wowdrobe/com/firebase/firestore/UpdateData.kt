package app.wowdrobe.com.firebase.firestore

import android.content.Context
import android.widget.Toast
import app.wowdrobe.com.communities.ui.DummyCards
import app.wowdrobe.com.communities.ui.RegistrationForm
import app.wowdrobe.com.tags.Groups
import app.wowdrobe.com.tags.TagWithoutTips
import com.google.firebase.firestore.FirebaseFirestore

fun updateInfoToFirebase(
    context: Context,
    name: String?,
    email: String?,
    phoneNumber: String?,
    gender: String?,
    organization: String?,
    address: String?,
    pointsEarned: Int,
    pointsRedeemed: Int,
    noOfTimesReported: Int = 0,
    noOfTimesCollected: Int = 0,
    noOfTimesActivity: Int = 0,
    communities: List<String> = emptyList()
) {
    val profile = ProfileInfo(
        name,
        email,
        phoneNumber,
        gender,
        organization,
        address,
        pointsEarned,
        pointsRedeemed,
        noOfTimesReported,
        noOfTimesCollected,
        noOfTimesActivity,
        communities
    )

    val db = FirebaseFirestore.getInstance()
    email?.let {
        db.collection("ProfileInfo").document(it).set(profile)
            .addOnSuccessListener {

                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Process Failed : " + exception.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

}

fun updateWasteToFirebase(
    context: Context,
    latitude: Double,
    longitude: Double,
    imagePath: String,
    timeStamp: Long,
    userEmail: String,
    address: String,
    tags: List<TagWithoutTips> = emptyList(),
) {
    val wasteItem = WasteItem(
        latitude, longitude, imagePath, timeStamp, userEmail, address, tags
    )

    val db = FirebaseFirestore.getInstance()
    timeStamp.let {
        db.collection("AllWastes").document(it.toString()).set(wasteItem)
            .addOnSuccessListener {

                Toast.makeText(context, "Added to sellers list", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Fail to Report Waste " + exception.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }


}

fun updateCollectedWasteToFirebase(
    context: Context,
    latitude: Double,
    longitude: Double,
    imagePath: String,
    timeStamp: Long,
    userEmail: String,
    address: String,
    isWasteCollected: Boolean,
    allWasteCollected: Boolean,
    feedBack: String,
    beforeCollectedPath: String? = null
) {
    val wasteItem = CollectedWasteItem(
        latitude,
        longitude,
        imagePath,
        timeStamp,
        userEmail,
        address,
        isWasteCollected,
        allWasteCollected,
        feedBack,
        beforeCollectedPath
    )

    val db = FirebaseFirestore.getInstance()
    timeStamp.let {
        db.collection("TempCollectedWastes").document(it.toString()).set(wasteItem)
            .addOnSuccessListener {

                Toast.makeText(context, "Added to sellers list", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Fail to Report Waste " + exception.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }


}

fun updateCommunitiesToFirebase(
    communities: RegistrationForm
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("Communities").document(communities.name).set(communities)
        .addOnSuccessListener {
            println("Communities Updated successfully..")

        }.addOnFailureListener { exception ->
            println("Fail to update Communities : " + exception.message)
        }

}

fun updateTagsToFirebase(
    tags: List<Groups>
) {
    val db = FirebaseFirestore.getInstance()
    tags.forEach {
        db.collection("TagGroups").document(it.name).set(it)
            .addOnSuccessListener {
                println("Communities Updated successfully..")

            }.addOnFailureListener { exception ->
                println("Fail to update Communities : " + exception.message)
            }
    }
}

fun updateCommunityToFirebase(
    community: DummyCards
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("Communities").document(community.name).set(community)
        .addOnSuccessListener {
            println("Communities Updated successfully..")

        }.addOnFailureListener { exception ->
            println("Fail to update Communities : " + exception.message)
        }
}



