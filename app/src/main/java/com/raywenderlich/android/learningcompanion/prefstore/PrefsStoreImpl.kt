package com.raywenderlich.android.learningcompanion.prefstore

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.raywenderlich.android.learningcompanion.di.PREFS_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val STORE_NAME = "learning_data_store"

class PrefsStoreImpl @Inject constructor(
        @ApplicationContext applicationContext: Context
) : PrefsStore {
    private val dataStore = applicationContext.createDataStore(
            name = STORE_NAME,
            migrations = listOf(SharedPreferencesMigration(applicationContext, PREFS_NAME))
    )

    override fun isNightMode(): Flow<Boolean> = dataStore.data.catch { exception ->
        // dataStore.data throws an IOException if it can't read the data
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[PreferencesKeys.NIGHT_MODE_KEY] ?: false }

    override suspend fun toggleNightMode() {
        dataStore.edit {
            it[PreferencesKeys.NIGHT_MODE_KEY] = !(it[PreferencesKeys.NIGHT_MODE_KEY] ?: false)
        }
    }

    private object PreferencesKeys {
        val NIGHT_MODE_KEY = preferencesKey<Boolean>("dark_theme_enabled")
    }
}