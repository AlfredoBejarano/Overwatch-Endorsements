package com.alfredobejarano.endorsements.viewmodel

import com.alfredobejarano.endorsements.repository.remote.Platforms
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Test suite for the [ProfileViewModel] class.
 * @author Alfredo Bejarano
 * @since Jul 24th, 2018 - 10:02 PM
 */
@RunWith(JUnit4::class)
class ProfileViewModelTest {
    /**
     * Value for testing a valid player element.
     */
    private val mTestPlayer = "Beji#11990"

    /**
     * Tests the retrieving of a valid profile data and
     * the extraction of the endorsements data.
     */
    @Test
    fun getProfileData() {
        // Create a view model for this test case.
        val testViewModel = ProfileViewModel()
        // Check when the player icon value gets changed, it is not null.
        testViewModel.playerIcon.observeForever {
            assert(it != null)
        }
        // Check when the endorsement level value gets changed, it is not null.
        testViewModel.endorsementLevel.observeForever {
            assert(it != null)
        }
        // Check when the sportsmanship value gets changed, it is not null.
        testViewModel.sportsmanship.observeForever {
            assert(it != null)
        }
        // Check when the good teammate value gets changed, it is not null.
        testViewModel.goodTeammate.observeForever {
            assert(it != null)
        }
        // Check when the shot caller value gets changed, it is not null.
        testViewModel.shotCaller.observeForever {
            assert(it != null)
        }
        // Retrieve the player profile data.
        testViewModel.getProfileData(Platforms.PC, mTestPlayer)
    }
}