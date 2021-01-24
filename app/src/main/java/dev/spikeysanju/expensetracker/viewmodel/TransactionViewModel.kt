package dev.spikeysanju.expensetracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.spikeysanju.expensetracker.datastore.UIModeDataStore
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.repo.TransactionRepo
import dev.spikeysanju.expensetracker.utils.ViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TransactionViewModel(
    application: Application,
    private val transactionRepo: TransactionRepo
) :
    AndroidViewModel(application) {

    private val _transactionFilter = MutableStateFlow("Overall")
    val transactionFilter = _transactionFilter.asStateFlow()

    private val _uiState = MutableStateFlow<ViewState>(ViewState.Loading)

    // UI collect from this stateFlow to get the state updates
    val uiState: StateFlow<ViewState> = _uiState

    // init datastore
    private val uiModeDataStore = UIModeDataStore(application)

    // get ui mode
    val getUIMode = uiModeDataStore.uiMode

    // save ui mode
    fun saveToDataStore(isNightMode: Boolean) {
        viewModelScope.launch(IO) {
            uiModeDataStore.saveToDataStore(isNightMode)
        }
    }

    // insert transaction
    fun insertTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepo.insert(transaction)
    }

    // update transaction
    fun updateTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepo.update(transaction)
    }

    // delete transaction
    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepo.delete(transaction)
    }

    // get all transaction
    fun getAllTransaction(type: String) = viewModelScope.launch {
        transactionRepo.getAllSingleTransaction(type).collect { result ->
            if (result.isNullOrEmpty()) {
                _uiState.value = ViewState.Empty
            } else {
                _uiState.value = ViewState.Success(result)
                Log.i("Filter", "Transaction filter is ${transactionFilter.value}")
            }
        }
    }

    fun allIncome() {
        _transactionFilter.value = "Income"
    }

    fun allExpense() {
        _transactionFilter.value = "Expense"
    }

    fun overall() {
        _transactionFilter.value = "Overall"
    }
}

