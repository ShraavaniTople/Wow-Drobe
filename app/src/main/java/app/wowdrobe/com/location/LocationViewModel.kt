package app.wowdrobe.com.location

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.wowdrobe.com.ktorClient.Resource
import app.wowdrobe.com.ktorClient.imageFromText.ImageFromText
import app.wowdrobe.com.ktorClient.repository.PlacesRepository
import app.wowdrobe.com.rewards.Level
import app.wowdrobe.com.tags.Tag
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    application: Application,
    private val repository: PlacesRepository,
    private val locationTracker: LocationTracker,
) : AndroidViewModel(application) {
    private val locationClient = LocationClientProvider(
        application.applicationContext,
        LocationServices.getFusedLocationProviderClient(application.applicationContext)
    )
    var locationState: MutableStateFlow<String> = MutableStateFlow("Location Not Found")
    var isDark by mutableStateOf(false)
    var showLevelDialog by mutableStateOf(false)
    var pointsEarned by mutableStateOf(0)
    var currentProgress by mutableStateOf(0f)
    var remainingPoints by mutableStateOf(0)
    var latitude by mutableStateOf(0.0)
    var theirLatitude = mutableStateOf(0.0)
    var longitude by mutableStateOf(0.0)
    var theirLongitude = mutableStateOf(0.0)
    var locationNo = mutableStateOf("")
    var address = mutableStateOf("")
    var distance = mutableStateOf("")
    var time = mutableStateOf("")
    var tags = mutableStateOf(listOf<Tag>())
    var wastePhoto = mutableStateOf("")
    var beforeCollectedPath = mutableStateOf("")
    var rewardImage = mutableStateOf("")
    var rewardTitle = mutableStateOf("")
    var rewardDescription = mutableStateOf("")
    var rewardNoOfPoints = mutableStateOf(0)
    var listOfAddresses by mutableStateOf(mutableListOf<String?>(null))
    var currentLevel = mutableStateOf<Level?>(null)

    val result = MutableLiveData<String>()

    private val _imageState = MutableStateFlow<ImageState>(ImageState.NotStarted)
    val imageState: StateFlow<ImageState> = _imageState.asStateFlow()

    val imagePrompt: MutableState<String> = mutableStateOf("")


    fun getPlaces() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let {
                repository.getPlaces("${it.latitude},${it.longitude}").let { resource ->
                    when (resource) {
                        is Resource.Failure -> {
                            println("API is Failed")
                            println("API is ${resource.exception.message}")
                        }

                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            resource.result.items?.forEach { address ->
                                listOfAddresses.add(address?.address?.label)
                            }
                            latitude = it.latitude
                            longitude = it.longitude
                            println("New Location is ${it.longitude} & ${it.latitude}")
                        }
                    }
                }
            }
        }
    }

    fun updateLocation() {
        viewModelScope.launch {
            locationClient.getLocationUpdates(2000L).collectLatest { location ->
                println("New Location is ${location.longitude} & ${location.latitude}")
                locationState
                    .emit("latitude = ${location.latitude} & longitude = ${location.longitude}")
            }
        }
    }

    fun getCurrentLevel(points: Int, levels: List<Level>) {
        // Sort the levels by their pointOfAchievements in ascending order
        val sortedLevels = levels.sortedByDescending { it.pointOfAchievements }

        for (level in sortedLevels) {
            if (points >= level.pointOfAchievements) {
                // User has enough points to reach this level
                currentLevel.value = level
                return
            }
        }
    }

    fun getCurrentLevelProgress(points: Int, levels: List<Level>): Float {
        val nextLevelIndex = currentLevel.value?.number ?: 1
        val nextLevel = if (nextLevelIndex < levels.size) levels[nextLevelIndex] else null
        currentLevel.value?.let { currentLevel ->
            if (nextLevel != null && points >= currentLevel.pointOfAchievements) {
                val levelRange = nextLevel.pointOfAchievements - currentLevel.pointOfAchievements
                if (levelRange > 0) {
                    Log.i("Next Level Index Tage", nextLevelIndex.toString())
                    return points / levels[nextLevelIndex].pointOfAchievements.toFloat()
                }
            }
        }

        return 0f
    }

    fun isNewLevelUnlocked(currentLevel: Level, points: Int, levels: List<Level>): Boolean {
        val nextLevelIndex = currentLevel.number
        val nextLevel = if (nextLevelIndex < levels.size) levels[nextLevelIndex] else null

        return nextLevel != null && points >= nextLevel.pointOfAchievements
    }

    fun pointsRemainingForNextLevel(points: Int, levels: List<Level>): Int {
        val nextLevelIndex = currentLevel.value?.number
        if (nextLevelIndex != null) {
            if (nextLevelIndex < levels.size) {
                Log.i("Next Level Index", nextLevelIndex.toString())
                val nextLevel = levels[nextLevelIndex]
                Log.i("Next Level Index Tage", points.toString())
                Log.i("Next Level Index Tags", nextLevel.pointOfAchievements.toString())
                return nextLevel.pointOfAchievements - points
            }
        }
        return 0 // No more levels, return 0 points
    }

    fun fetchImage(prompt: String) {
        _imageState.value = ImageState.Loading
        viewModelScope.launch {
            try {
                val imageData = repository.textToImage(
                    ImageFromText(
                        inputs = prompt
                    )
                )
                if (imageData != null) {
                    _imageState.value = ImageState.Loaded(imageData)
                    Log.i("Imagesssssssss", _imageState.value.toString())
                } else {
                    _imageState.value = ImageState.Error(Exception("Something went wrong."))
                }
            } catch (e: Exception) {
                _imageState.value = ImageState.Error(e)
            }
        }
    }



}

sealed class ImageState {
    object Loading : ImageState()
    data class Loaded(val imageData: ByteArray) : ImageState() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Loaded

            if (!imageData.contentEquals(other.imageData)) return false

            return true
        }

        override fun hashCode(): Int {
            return imageData.contentHashCode()
        }
    }

    data class Error(val exception: Exception) : ImageState()

    object NotStarted : ImageState()
}