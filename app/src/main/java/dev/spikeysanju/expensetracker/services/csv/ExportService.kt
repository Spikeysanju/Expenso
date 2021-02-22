package dev.spikeysanju.expensetracker.services.csv

import androidx.annotation.WorkerThread
import androidx.core.net.toUri
import com.opencsv.CSVWriter
import com.opencsv.bean.StatefulBeanToCsvBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileWriter

/****
 * Author : Ch8n
 * Created-on : 10-02-2021
 */

object ExportService {

    fun <T : Exportable> export(type: Exports, content: List<T>): Flow<String> = when (type) {
        is Exports.CSV -> writeToCSV<T>(type.csvConfig, content)
    }

    // todo use any design pattern to abstract away complexity
    @WorkerThread
    private fun <T : Exportable> writeToCSV(csvConfig: CsvConfig, content: List<T>) = flow<String> {
        with(csvConfig) {
            hostPath.ifEmpty { throw IllegalStateException("Wrong Path") }
            val hostDirectory = File(hostPath)
            if (!hostDirectory.exists()) {
                hostDirectory.mkdirs()
            }
            val csvFile = File("${hostDirectory.path}/$fileName")
            val fileUri = csvFile.toUri().toString()
            val csvWriter = CSVWriter(FileWriter(csvFile))
            StatefulBeanToCsvBuilder<T>(csvWriter)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build()
                .write(content)
            csvWriter.close()
            emit(fileUri)
        }
    }
}
