package dev.spikeysanju.expensetracker.view.settings


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.spikeysanju.expensetracker.data.local.datastore.SettingsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application
) :
    AndroidViewModel(application) {

    // init datastore
    private val settingsDataStore = SettingsDataStore(application)

    // get bio-metric preference
    val bioMetricPreference = settingsDataStore.biometric

    fun setBioMetricLock(bioMetricLock: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsDataStore.saveToDataStore(bioMetricLock)
        }
    }

}
