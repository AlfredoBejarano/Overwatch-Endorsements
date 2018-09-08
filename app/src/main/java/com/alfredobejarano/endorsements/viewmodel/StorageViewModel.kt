package com.alfredobejarano.endorsements.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.alfredobejarano.endorsements.model.PlayerProfile
import com.alfredobejarano.endorsements.repository.local.AppDatabase
import kotlin.concurrent.thread

class StorageViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * MutableLiveData property for retrieving a session.
     */
    val session: MutableLiveData<PlayerProfile> = MutableLiveData()

    /**
     * Retrieve the current local session.
     */
    fun getStoredProfile() = thread(start = true, name = "${this::class.java} thread") {
        // Retrieve a stored session from the database.
        val localSession = AppDatabase
                .getInstance(getApplication())
                .playerProfileDao()
                .getPlayerProfile()
        // Set the retrieved session as the value of the session property.
        session.postValue(localSession)
    }

    /**
     * Stores a player profile object into the local storage database.
     */
    fun storePlayerProfile(playerProfile: PlayerProfile) = thread(start = true,
            name = "${this.javaClass.name} thread") {
        AppDatabase.getInstance(getApplication())
                .playerProfileDao()
                .storePlayerProfile(playerProfile)
    }
}