package app.wowdrobe.com.navigation

sealed class Screens(val route: String) {


    object Onboarding : Screens("onboarding")
    object CompleteProfile : Screens("completeProfile")
    object Community : Screens("community")
    object SettingUp : Screens("settingUp")
    object ReportWaste : Screens("reportWaste")
    object CollectWasteLists : Screens("collectWasteLists")
    object CollectWasteInfo : Screens("collectWasteInfo")
    object CollectedWasteSuccess : Screens("collectWasteSuccess")
    object AllActivities : Screens("allActivities")
    object MyRecordings : Screens("myRecordings")
    object StopRecording : Screens("stopRecording")
    object Rewards : Screens("rewards")
    object RewardsDetails : Screens("rewardsDetails")
    object ClaimedRewards : Screens("claimedRewards")
    object LoginScreen : Screens("login")
    object Dashboard : Screens("dashboard")
    object Profile : Screens("profile")
    object Splash : Screens("splash")
    object AIStudio : Screens("aiStudio")

}
