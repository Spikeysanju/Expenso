package dev.spikeysanju.expensetracker.view.statistics

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.spikeysanju.expensetracker.repo.TransactionRepo
import dev.spikeysanju.expensetracker.utils.viewState.DetailState
import dev.spikeysanju.expensetracker.utils.viewState.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject



// not sure if i should create new viewmodel for every fragment or use a common one. i am leaving this just in case
// have to use separate viewmodels
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    application: Application,
    private val transactionRepo: TransactionRepo
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<ViewState>(ViewState.Loading)
    // UI collect from this stateFlow to get the state updates
    val uiState: StateFlow<ViewState> = _uiState

    fun getAllTransaction(type: String) = viewModelScope.launch {
        transactionRepo.getAllSingleTransaction(type).collect { result ->
            if (result.isNullOrEmpty()) {
                _uiState.value = ViewState.Empty
            } else {
                _uiState.value = ViewState.Success(result)

            }
        }
    }
}
