package dev.spikeysanju.expensetracker.services.csv

import android.os.Environment
import androidx.annotation.WorkerThread
import com.opencsv.CSVWriter
import com.opencsv.bean.StatefulBeanToCsvBuilder
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileWriter
import java.text.DateFormat

// todo
// add permission check
// add scoped storage
// add test cases for file creation

/****
 * Author : Ch8n
 * Created-on : 10-02-2021
 */
sealed class Exports {
    data class CSV(val csvConfig: CsvConfig) : Exports()
}

data class CsvConfig(
    val prefix: String = "expenso",
    val suffix: String = DateFormat
        .getDateTimeInstance()
        .format(System.currentTimeMillis())
        .toString()
        .replace(" ", "_"),
    val fileName: String = "$prefix-$suffix.csv",
    @Suppress("DEPRECATION")
    val hostPath: String = Environment
        .getExternalStorageDirectory()?.absolutePath?.plus("/Documents/Expenso") ?: ""
)

object ExportService {

    fun <T> export(type: Exports, content: List<T>) = when (type) {
        is Exports.CSV -> writeToCSV<T>(type.csvConfig, content)
    }

    // todo use any design pattern to abstract away complexity
    @WorkerThread
    private fun <T> writeToCSV(csvConfig: CsvConfig, content: List<T>) = flow<Boolean> {
        with(csvConfig) {
            hostPath.ifEmpty { throw IllegalStateException("Wrong Path") }
            val hostDirectory = File(hostPath)
            if (!hostDirectory.exists()) {
                hostDirectory.mkdir()
            }
            val csvFile = File("${hostDirectory.path}/$fileName")
            val csvWriter = CSVWriter(FileWriter(csvFile))
            StatefulBeanToCsvBuilder<T>(csvWriter)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build()
                .write(content)
            csvWriter.close()
        }
        emit(true)
    }
}
