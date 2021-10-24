package dev.spikeysanju.expensetracker.services.exportcsv

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.opencsv.CSVWriter
import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.StatefulBeanToCsvBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.spikeysanju.expensetracker.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.net.URI
import javax.inject.Inject

class CreateCsvContract : ActivityResultContract<String, Uri?>() {

    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/csv"
            putExtra(Intent.EXTRA_TITLE, "$input.csv")
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.data
    }

}

data class TransactionsCSV(
    @CsvBindByName(column = "title")
    val title: String,
    @CsvBindByName(column = "amount")
    val amount: Double,
    @CsvBindByName(column = "transactionType")
    val transactionType: String,
    @CsvBindByName(column = "tag")
    val tag: String,
    @CsvBindByName(column = "date")
    val date: String,
    @CsvBindByName(column = "note")
    val note: String,
    @CsvBindByName(column = "createdAt")
    val createdAtDate: String
)

fun List<Transaction>.toCsv() = map {
    TransactionsCSV(
        title = it.title,
        amount = it.amount,
        transactionType = it.transactionType,
        tag = it.tag,
        date = it.date,
        note = it.note,
        createdAtDate = it.createdAtDateFormat,
    )
}

class ExportService(
    private val appContext: Context
) {

    @WorkerThread
    fun <T> writeToCSV(csvFileUri: Uri, content: List<T>) = flow<String> {
        val fileDescriptor = appContext.contentResolver.openFileDescriptor(csvFileUri, "w")
        if (fileDescriptor != null) {
            fileDescriptor.use {
                val csvWriter = CSVWriter(FileWriter(it.fileDescriptor))
                StatefulBeanToCsvBuilder<T>(csvWriter)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build()
                    .write(content)
                csvWriter.close()
                emit(csvFileUri.toString())
            }
        } else {
            throw IllegalStateException("failed to read fileDescriptor")
        }
    }
}
