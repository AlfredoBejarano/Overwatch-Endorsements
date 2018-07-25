package com.alfredobejarano.endorsements.source

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.HttpURLConnection
import java.util.*

/**
 * Class that provides utility access for the source code of a player career profile.
 */
class CareerProfile {
    companion object {
        /**
         * Defines how many seconds needs to pass for the connection to close itself.
         */
        private const val TIMEOUT_MILLISECONDS = 10000

        /**
         * Defines a user agent for the connection, it will tell the overwatch page which HTML code to send.
         */
        private const val USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21"

        /**
         * Function that builds the career profile URL given the player data.
         * @param platform The platform that the player is playing on.
         * @param userName The player BattleTag, Gamertag or PSN ID.
         * @return String value for the desired career profile.
         */
        fun buildProfileURL(platform: Platforms, userName: String): String {
            // Get the path value for the desired platform using the PLatforms enum class.
            val platformPath = platform.name.toLowerCase(Locale.getDefault())
            // Build the base URL using the given platform value.
            val baseAddress = "https://playoverwatch.com/en-us/career/$platformPath/"
            // Append the player platform username to the base address.
            return if (platform == Platforms.PC) {
                // Change the hash (#) symbol with a dash (-) for PC profiles.
                baseAddress + userName.replace("#", "-")
            } else {
                // Just append the username if it is for another platform.
                baseAddress + userName
            }
        }

        /**
         * Function that returns .
         * @param platform The platform that the player is playing on.
         * @param userName The player BattleTag, Gamertag or PSN ID.
         * @return Jsoup document class containing the HTML source code of the retrieved page.
         */
        fun getProfileHTMLCode(platform: Platforms, userName: String): Document? {
            // Retrieve the career profile URl using the buildProfileURL() function.
            val profileURL = buildProfileURL(platform, userName)
            // Establish the details for the Jsoup connection.
            val connection = Jsoup.connect(profileURL)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MILLISECONDS)
            // Execute said connection.
            val response = connection.execute()
            // Check if the connection HTTP status code is a 200 status code.
            return if (response?.statusCode() == HttpURLConnection.HTTP_OK) {
                // Get the document of whatever HTML spaghetti code was retrieved.
                connection.get()
            } else {
                // If something is going wrong with the Overwatch page, return a null document.
                null
            }
        }
    }
}