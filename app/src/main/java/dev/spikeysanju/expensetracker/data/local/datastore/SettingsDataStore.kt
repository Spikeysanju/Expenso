package dev.spikeysanju.expensetracker.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton


class SettingsDataStore(context: Context) :
    PrefsDataStore(
        context,
        PREF_FILE_SETTINGS
    ),
    SettingsImpl {

    // used to get the data from datastore
    override val biometric: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            val biometric = preferences[BIOMETRIC_KEY] ?: false
            biometric
        }

    // used to save the bio-metric preference to datastore
    override suspend fun saveToDataStore(isBiometricEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[BIOMETRIC_KEY] = isBiometricEnabled
        }
    }

    companion object {
        private const val PREF_FILE_SETTINGS = "settings_preference"
        private val BIOMETRIC_KEY = booleanPreferencesKey("biometric_mode")
    }
}

@Singleton
interface SettingsImpl {
    val biometric: Flow<Boolean>
    suspend fun saveToDataStore(isBiometricEnabled: Boolean)
}
