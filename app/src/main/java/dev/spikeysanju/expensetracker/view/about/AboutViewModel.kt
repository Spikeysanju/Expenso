package dev.spikeysanju.expensetracker.view.about

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AboutViewModel : ViewModel() {
    private val _url = MutableStateFlow("https://github.com/Spikeysanju/Expenso")
    val url: StateFlow<String> = _url

    fun launchLicense() {
        _url.value = "https://github.com/Spikeysanju/Expenso/blob/master/LICENSE"
    }

    fun launchRepository() {
        _url.value = "https://github.com/Spikeysanju/Expenso"
    }
}
