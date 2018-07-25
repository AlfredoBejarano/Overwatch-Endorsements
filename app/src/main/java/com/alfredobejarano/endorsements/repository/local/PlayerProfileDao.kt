package com.alfredobejarano.endorsements.repository.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.alfredobejarano.endorsements.model.PlayerProfile


/**
 * Data Access Object for the [PlayerProfile] class.
 * @author Alfredo Bejarano
 * @since Jul 24th, 2018 - 10:25 PM
 */
@Dao
interface PlayerProfileDao {
    /**
     * Stores a player profile into the local database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storePlayerProfile(playerProfile: PlayerProfile)

    /**
     * Retrieves the player profile session from the profiles table.
     * @return [PlayerProfile] The current player profile.
     */
    @Query("SELECT * from profiles ORDER BY id LIMIT 1")
    fun getPlayerProfile(): PlayerProfile?


    /**
     * Deletes everything from the profiles table.
     */
    @Query("DELETE from profiles")
    fun nukeProfilesTable()
}