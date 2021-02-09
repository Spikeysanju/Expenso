package dev.spikeysanju.expensetracker.services.csv

import android.content.Context
import android.os.Environment.DIRECTORY_DOCUMENTS
import android.util.Log
import androidx.annotation.WorkerThread
import com.opencsv.CSVWriter
import com.opencsv.bean.StatefulBeanToCsvBuilder
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.services.csv.adapters.TransactionsCSV
import dev.spikeysanju.expensetracker.services.csv.adapters.toCsv
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileWriter
import java.text.DateFormat


class ExportCSV (private val context: Context) {

    private val TAG = "ExportCSV"

    private val currentDate = DateFormat
        .getDateTimeInstance()
        .format(System.currentTimeMillis())
        .toString()
        .replace(" ", "_")

    private val hostPath
        get() = context.applicationContext
            .getExternalFilesDir(DIRECTORY_DOCUMENTS)?.path?.plus("/Expenso") ?: ""

    private val CSV_FILE_NAME
        get() = "Expenso_$currentDate.csv"


    @WorkerThread
    fun writeTransactions(transactions: List<Transaction>) = flow<Boolean> {
        hostPath.ifEmpty { throw IllegalStateException("Wrong Path") }

        val hostDirectory = File(hostPath)
        if (!hostDirectory.exists()) {
            hostDirectory.mkdir()
        }

        val expensoCSV = File("${hostDirectory.path}/${CSV_FILE_NAME}")
        CSVWriter(FileWriter(expensoCSV))
            .apply {
                StatefulBeanToCsvBuilder<TransactionsCSV>(this)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build()
                    .write(transactions.toCsv())
                close()
            }

        emit(true)
    }.catch { error: Throwable ->
        Log.e(TAG, "Failed Exporting", error)
        emit(false)
    }

}
