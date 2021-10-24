package dev.spikeysanju.expensetracker.utils.viewState

sealed class ExportState {
    object Loading : ExportState()
    object Empty : ExportState()
    data class Success(val fileUri: String) : ExportState()
    data class Error(val exception: Throwable) : ExportState()
}
