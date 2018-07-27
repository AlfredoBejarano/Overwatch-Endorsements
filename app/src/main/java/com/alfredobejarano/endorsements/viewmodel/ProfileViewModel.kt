package com.alfredobejarano.endorsements.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.alfredobejarano.endorsements.repository.remote.CareerProfile
import com.alfredobejarano.endorsements.repository.remote.Platforms
import org.jsoup.nodes.Element
import kotlin.concurrent.thread

/**
 * Simple [ViewModel] class that retrieves a career
 * profile document and extract the endorsements data.
 * @author Alfredo Bejarano
 * @since Jul 24th, 2018 - 09:40 PM
 */
class ProfileViewModel : ViewModel() {
    /**
     * Constants for the necessary class names inside the career profile HTML document.
     */
    companion object {
        private const val IMG_SRC_ATTRIBUTE = "src"
        private const val ENDORSEMENT_LEVEL_ELEMENT_CLASS = "u-center"
        private const val PLAYER_ICON_ELEMENT_CLASS = "player-portrait"
        private const val ENDORSEMENT_PERCENTILE_ATTRIBUTE = "data-value"
        private const val PLAYER_STATISTICS_ELEMENT_CLASS = "masthead-player-progression hide-for-lg"
        private const val ENDORSEMENT_ELEMENT_CLASS = "EndorsementIcon-border EndorsementIcon-border--%1\$s"
    }

    val loading: MutableLiveData<Int> = MutableLiveData()
    val status: MutableLiveData<Status> = MutableLiveData()
    val playerIcon: MutableLiveData<String> = MutableLiveData()
    val shotCaller: MutableLiveData<Double> = MutableLiveData()
    val goodTeammate: MutableLiveData<Double> = MutableLiveData()
    val sportsmanship: MutableLiveData<Double> = MutableLiveData()
    val endorsementLevel: MutableLiveData<String> = MutableLiveData()

    /**
     * This function crawls playoverwatch.com and extracts a player endorsement data from its career profile page.
     * @param platform The platform that the player is playing on.
     * @param userName The player BattleTag, Gamertag or PSN ID.
     */
    fun getProfileData(platform: Platforms, userName: CharSequence?) = thread(start = true) {
        // Notify that the ViewModel is performing an operation.
        loading.postValue(View.VISIBLE)
        // Get the document using the given profile data.
        val document = CareerProfile.getProfileHTMLCode(platform, userName?.toString() ?: "")
        // If the document is not null, it means the page got a 200 HTTP code response.
        document?.let {
            // Get the player statistics div element from the document.
            it.getElementsByClass(PLAYER_STATISTICS_ELEMENT_CLASS)?.first()?.let {
                // Extract the endorsement level.
                val level = it.getElementsByClass(ENDORSEMENT_LEVEL_ELEMENT_CLASS).first().text()
                endorsementLevel.postValue(level)
                // Post the value of the player profile icon from the document.
                val iconURL = document.getElementsByClass(PLAYER_ICON_ELEMENT_CLASS).first().attr(IMG_SRC_ATTRIBUTE)
                playerIcon.postValue(iconURL)
                // Extract the values for different endorsements.
                shotCaller.postValue(getEndorsement(it, Endorsements.SHOTCALLER))
                goodTeammate.postValue(getEndorsement(it, Endorsements.TEAMMATE))
                sportsmanship.postValue(getEndorsement(it, Endorsements.SPORTSMANSHIP))
                // Report the operation status.
                status.postValue(Status.STATUS_OK)
            } ?: run {
                // If the player statistics element is not found within the document, it means no player was found.
                status.postValue(Status.STATUS_PROFILE_NOT_FOUND)
            }
        } ?: run {
            // If the document is null, it means there is something wrong within the Overwatch page.
            status.postValue(Status.STATUS_BLIZZARD_DOWN)
        }
        loading.postValue(View.GONE)
    }

    /**
     * This function extracts a given endorsement percentage value.
     * @param htmlElement Player statistics element extracted from its Career profile page.
     * @param endorsement The expected endorsement to extract its value for.
     */
    private fun getEndorsement(htmlElement: Element, endorsement: Endorsements) : Double {
        // Build the wanted endorsement class name.
        val endorsementClassName = String.format(ENDORSEMENT_ELEMENT_CLASS, endorsement.name.toLowerCase())
        // Get the endorsement element.
        val endorsementElement = htmlElement.getElementsByClass(endorsementClassName).first()
        // Extract its decimal value.
        val endorsementPercentile = endorsementElement.attr(ENDORSEMENT_PERCENTILE_ATTRIBUTE)
        // Parse it into a double.
        return endorsementPercentile.toDouble()
    }

    /**
     * Internal enum class that defines the three different Endorsements categories.
     */
    internal enum class Endorsements {
        /**
         * Value for the "good teammate" endorsement.
         */
        TEAMMATE,
        /**
         * Value for the "shot caller" endorsement.
         */
        SHOTCALLER,
        /**
         * Value for the "sportsmanship" endorsement.
         */
        SPORTSMANSHIP
    }

    /**
     * Enum class that defines the status of the profile request.
     */
    enum class Status {
        /**
         * Status that defines the request was made successfully.
         */
        STATUS_OK,
        /**
         * Status for when the blizzard page is not available.
         */
        STATUS_BLIZZARD_DOWN,
        /**
         * Status for a profile that has not been found.
         */
        STATUS_PROFILE_NOT_FOUND
    }
}