package com.alfredobejarano.endorsements.source

import com.alfredobejarano.endorsements.repository.remote.CareerProfile
import com.alfredobejarano.endorsements.repository.remote.Platforms
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Test suite for the [CareerProfile] class.
 * @author Alfredo Bejarano
 * @since Jul 24th, 2018 - 08:50 PM
 */
@RunWith(JUnit4::class)
class CareerProfileTest {
    // This property defines the input of a PC profile.
    private val testPCPlayer = "Beji#11990"
    // This property defines the input of a Console profile.
    private val testXBLPLayer = "xX-Beji-Xx"
    // This property defines the correct URL of a PC profile.
    private val expectedPCURL = "https://playoverwatch.com/en-us/career/pc/Beji-11990"
    // This property defines the correct of a console profile.
    private val expectedConsoleURL = "https://playoverwatch.com/en-us/career/xbl/xX-Beji-Xx"

    /**
     * This function tests if the [CareerProfile.buildProfileURL] function builds URLs properly.
     */
    @Test
    fun buildProfileURLTest() {
        // Assert a PC user asking for a PC url has been built correctly.
        assert(expectedPCURL == CareerProfile.buildProfileURL(Platforms.PC, testPCPlayer))
        // Assert a XBL user asking for a XBL url has been built correctly.
        assert(expectedConsoleURL == CareerProfile.buildProfileURL(Platforms.XBL, testXBLPLayer))
    }

    /**
     * This function tests that the [CareerProfile.getProfileHTMLCode]
     * function returns a HTML document correctly.
     */
    @Test
    fun getProfileHTMLCodeTest() {
        // Assert that a requested profile gets a Document.
        assert(CareerProfile.getProfileHTMLCode(Platforms.PC, testPCPlayer) != null)
        // Also assert that an non-existing profile returns a Document (the player not found page).
        assert(CareerProfile.getProfileHTMLCode(Platforms.PSN, testXBLPLayer) != null)
    }
}