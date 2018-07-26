package com.alfredobejarano.endorsements.repository.local.typeconverters

import android.arch.persistence.room.TypeConverter
import com.alfredobejarano.endorsements.repository.remote.Platforms

/**
 * Class that defines how to retrieve and store [Platforms] values using Room.
 */
class PlatformsTypeConverter {
    /**
     * Type converter that defines how to store a [Platforms] value using Room.
     */
    @TypeConverter
    fun fromPlatform(platform: Platforms) = platform.name

    /**
     * Type converter that defines how to retrieve a [Platforms] from Room.
     */
    @TypeConverter
    fun toPlatform(name: String) = Platforms.valueOf(name)
}