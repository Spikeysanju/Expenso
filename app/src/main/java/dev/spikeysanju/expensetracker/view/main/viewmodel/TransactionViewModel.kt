package dev.spikeysanju.expensetracker.view.main.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.spikeysanju.expensetracker.data.local.datastore.UIModeDataStore
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.repo.TransactionRepo
import dev.spikeysanju.expensetracker.services.csv.CsvConfig
import dev.spikeysanju.expensetracker.services.csv.ExportService
import dev.spikeysanju.expensetracker.services.csv.Exports
import dev.spikeysanju.expensetracker.services.csv.adapters.TransactionsCSV
import dev.spikeysanju.expensetracker.services.csv.adapters.toCsv
import dev.spikeysanju.expensetracker.utils.viewState.DetailState
import dev.spikeysanju.expensetracker.utils.viewState.ExportState
import dev.spikeysanju.expensetracker.utils.viewState.ViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    application: Application,
    private val transactionRepo: TransactionRepo
) :
    AndroidViewModel(application) {

    private val _transactionFilter = MutableStateFlow("Overall")
    val transactionFilter: StateFlow<String> = _transactionFilter

    // state for export csv status
    private val _exportCsvState = MutableStateFlow<ExportState>(ExportState.Empty)
    val exportCsvState: StateFlow<ExportState> = _exportCsvState

    private val _uiState = MutableStateFlow<ViewState>(ViewState.Loading)
    private val _detailState = MutableStateFlow<DetailState>(DetailState.Loading)

    // UI collect from this stateFlow to get the state updates
    val uiState: StateFlow<ViewState> = _uiState
    val detailState: StateFlow<DetailState> = _detailState

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

    // export all Transactions to csv file
    fun exportTransactionsToCsv() = viewModelScope.launch(IO) {
        _exportCsvState.value = ExportState.Loading
        val transactions = transactionRepo.getAllTransactions().first()
        ExportService.export<TransactionsCSV>(
            type = Exports.CSV(CsvConfig()),
            content = transactions.toCsv()
        ).catch { error ->
            _exportCsvState.value = ExportState.Error(error)
        }.collect { uriString ->
            _exportCsvState.value = ExportState.Success(uriString)
        }
    }

    // get transaction by id
    fun getByID(id: Int) = viewModelScope.launch {
        _detailState.value = DetailState.Loading
        transactionRepo.getByID(id).collect { result: Transaction? ->
            if (result != null) {
                _detailState.value = DetailState.Success(result)
            }
        }
    }

    // delete transaction
    fun deleteByID(id: Int) = viewModelScope.launch {
        transactionRepo.deleteByID(id)
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
