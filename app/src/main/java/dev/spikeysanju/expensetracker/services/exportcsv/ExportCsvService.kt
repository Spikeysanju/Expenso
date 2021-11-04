package dev.spikeysanju.expensetracker.services.exportcsv

import android.content.Context
import android.net.Uri
import androidx.annotation.WorkerThread
import com.opencsv.CSVWriter
import com.opencsv.bean.StatefulBeanToCsvBuilder
import kotlinx.coroutines.flow.flow
import java.io.FileWriter
import javax.inject.Inject

class ExportCsvService @Inject constructor(
    private val appContext: Context
) {

    @WorkerThread
    fun <T> writeToCSV(csvFileUri: Uri, content: List<T>) = flow<Uri> {
        val fileDescriptor = appContext.contentResolver.openFileDescriptor(csvFileUri, "w")
        if (fileDescriptor != null) {
            fileDescriptor.use {
                val csvWriter = CSVWriter(FileWriter(it.fileDescriptor))
                StatefulBeanToCsvBuilder<T>(csvWriter)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build()
                    .write(content)
                csvWriter.close()
                emit(csvFileUri)
            }
        } else {
            throw IllegalStateException("failed to read fileDescriptor")
        }
    }
}
