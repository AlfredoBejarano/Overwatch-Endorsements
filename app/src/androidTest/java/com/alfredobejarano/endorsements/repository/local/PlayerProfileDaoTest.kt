package com.alfredobejarano.endorsements.repository.local

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.alfredobejarano.endorsements.model.PlayerProfile
import com.alfredobejarano.endorsements.repository.remote.Platforms
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test suite for the ProfilePlayer DAO.
 * It test the implementation of said methods using an in-memory Room database.
 */
@RunWith(AndroidJUnit4::class)
open class PlayerProfileDaoTest {
    /**
     * Reference to a in-memory [AppDatabase] instance.
     */
    private lateinit var mTestDatabase: AppDatabase
    /**
     * [PlayerProfile] object for testing in this database.
     */
    private lateinit var mPlayerProfile: PlayerProfile

    /**
     * Initializes the in-memory database before running a test.
     */
    @Before
    fun initDb() {
        mTestDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).build()
        mPlayerProfile = PlayerProfile(1, "Beji#11990", Platforms.PC)
    }

    /**
     * Close the database after running a test.
     */
    @After
    fun closeDb() {
        mTestDatabase.close()
    }

    /**
     * Function that tests if a [PlayerProfile] session is stored correctly.
     */
    @Test
    fun createAndReadPlayerProfileTest() {
        // Store the test PlayerProfile object.
        mTestDatabase.playerProfileDao().storePlayerProfile(mPlayerProfile)
        // Retrieve the recently stored PlayerProfile.
        val storedPlayerProfile = mTestDatabase.playerProfileDao().getPlayerProfile()
        // Compare both PlayerProfile objects.
        assert(mPlayerProfile == storedPlayerProfile)
    }

    /**
     * Inserts a [PlayerProfile] record, nukes the table and reads the table again
     * asserting that the results are  null.
     */
    @Test
    fun nukeProfilesTableTest() {
        // Store the test PlayerProfile object.
        mTestDatabase.playerProfileDao().storePlayerProfile(mPlayerProfile)
        // Nuke the table
        mTestDatabase.playerProfileDao().nukeProfilesTable()
        // Check that a read query returns null.
        assert(mTestDatabase.playerProfileDao().getPlayerProfile() == null)
    }
}