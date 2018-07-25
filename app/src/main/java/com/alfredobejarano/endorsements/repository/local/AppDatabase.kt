package com.alfredobejarano.endorsements.repository.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.alfredobejarano.endorsements.model.PlayerProfile

/**
 * Singleton class that allows access to the app room database.
 * @author Alfredo Bejarano
 * @since Jul 24th, 2018 - 10:42 PM
 */
@Database(entities = [PlayerProfile::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Function that allows access too the player profile dao.
     */
    abstract fun playerProfileDao(): PlayerProfileDao

    companion object {
        /**
         * Property that allows singleton access to the app database.
         */
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Creates an instance of this Database, allowing singleton access.
         * @param context The context creating this singleton instance.
         * @return The instance of this AppDatabase.
         */
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        /**
         * Creates the database.
         * @param context The context creating this singleton instance.
         */
        private fun buildDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "")
                .fallbackToDestructiveMigration()
                .build()
    }
}