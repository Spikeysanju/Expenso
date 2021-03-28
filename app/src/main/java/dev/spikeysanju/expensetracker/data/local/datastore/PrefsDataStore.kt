package dev.spikeysanju.expensetracker.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore


abstract class PrefsDataStore(context: Context, fileName: String) {
    internal val dataStore: DataStore<Preferences> = context.createDataStore(fileName)
}
