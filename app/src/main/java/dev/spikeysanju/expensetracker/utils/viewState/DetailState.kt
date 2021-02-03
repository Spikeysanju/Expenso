package dev.spikeysanju.expensetracker.utils.viewState

import dev.spikeysanju.expensetracker.model.Transaction

sealed class DetailState {
    object Loading : DetailState()
    object Empty : DetailState()
    data class Success(val transaction: Transaction) : DetailState()
    data class Error(val exception: Throwable) : DetailState()
}
