package com.alfredobejarano.endorsements.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alfredobejarano.endorsements.source.CareerProfile
import com.alfredobejarano.endorsements.source.Platforms
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

    val playerIcon: MutableLiveData<String> = MutableLiveData()
    val shotCaller: MutableLiveData<Double> = MutableLiveData()
    val goodTeammate: MutableLiveData<Double> = MutableLiveData()
    val sportsmanship: MutableLiveData<Double> = MutableLiveData()
    val endorsementLevel: MutableLiveData<String> = MutableLiveData()

    fun getProfileData(platform: Platforms, userName: CharSequence?) = thread(start = true, name = "${this::class.java.name} thread") {
        val document = CareerProfile.getProfileHTMLCode(platform, userName?.toString() ?: "")
        document?.let {
            it.getElementsByClass(PLAYER_STATISTICS_ELEMENT_CLASS)?.first()?.let {
                // Extract the endorsement level.
                endorsementLevel.postValue(it.getElementsByClass(ENDORSEMENT_LEVEL_ELEMENT_CLASS).first().text())
                // Post the value of the player profile icon from the document.
                playerIcon.postValue(document.getElementsByClass(PLAYER_ICON_ELEMENT_CLASS).first().attr(IMG_SRC_ATTRIBUTE))
                // Extract the values for different endorsements.
                shotCaller.postValue(getEndorsement(it, Endorsements.SHOTCALLER))
                goodTeammate.postValue(getEndorsement(it, Endorsements.TEAMMATE))
                sportsmanship.postValue(getEndorsement(it, Endorsements.SPORTSMANSHIP))
            } ?: run {

            }
        } ?: run {

        }
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
}