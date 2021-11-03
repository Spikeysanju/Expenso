package dev.spikeysanju.expensetracker.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

val Context.themePrefDataStore by preferencesDataStore("ui_mode_pref")

class UIModeDataStore(context: Context) : UIModeImpl {

    private val dataStore = context.themePrefDataStore

    // used to get the data from datastore
    override val uiMode: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            val uiMode = preferences[UI_MODE_KEY] ?: false
            uiMode
        }

    // used to save the ui preference to datastore
    override suspend fun saveToDataStore(isNightMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[UI_MODE_KEY] = isNightMode
        }
    }

    companion object {
        private val UI_MODE_KEY = booleanPreferencesKey("ui_mode")
    }
}

@Singleton
interface UIModeImpl {
    val uiMode: Flow<Boolean>
    suspend fun saveToDataStore(isNightMode: Boolean)
}
