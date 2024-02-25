package app.wowdrobe.com.communities

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CommunitiesViewModel : ViewModel() {
    val expandedState = mutableStateOf(0f)
    val expandedState2 = mutableStateOf(0f)
    val currentPage = mutableStateOf(0)


    val communitiesTitle = mutableStateOf("")
    val communitiesLocation = mutableStateOf("")
    val communitiesDate = mutableStateOf("")
    val communitiesTime = mutableStateOf("")
    val communitesDescription = mutableStateOf("")
    val communititesOrgBy = mutableStateOf("")
    val communititesImage = mutableStateOf("")
}