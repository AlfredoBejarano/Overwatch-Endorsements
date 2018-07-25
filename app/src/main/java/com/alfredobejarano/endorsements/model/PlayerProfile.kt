package com.alfredobejarano.endorsements.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.alfredobejarano.endorsements.repository.remote.Platforms

/**
 * DTO class that will serve as an entity for storing a player profile data using Room.
 */
@Entity(tableName = "profiles")
data class PlayerProfile(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        @ColumnInfo(name = "player_tag")
        val userName: String,
        @ColumnInfo(name = "player_platform")
        val platform: Platforms)