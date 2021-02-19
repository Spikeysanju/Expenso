package dev.spikeysanju.expensetracker.services.csv

import android.os.Environment
import java.text.DateFormat

/****
 * Author : Ch8n
 * Created-on : 10-02-2021
 */

// marker interface for exportable entities
interface Exportable

sealed class Exports {
    data class CSV(val csvConfig: CsvConfig) : Exports()
}

data class CsvConfig(
    private val prefix: String = "expenso",
    private val suffix: String = DateFormat
        .getDateTimeInstance()
        .format(System.currentTimeMillis())
        .toString()
        .replace(" ", "_"),
    val fileName: String = "$prefix-$suffix.csv",
    @Suppress("DEPRECATION")
    val hostPath: String = Environment
        .getExternalStorageDirectory()?.absolutePath?.plus("/Documents/Expenso") ?: ""
)
