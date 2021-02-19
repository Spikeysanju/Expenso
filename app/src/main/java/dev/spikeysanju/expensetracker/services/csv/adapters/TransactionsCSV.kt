package dev.spikeysanju.expensetracker.services.csv.adapters

import com.opencsv.bean.CsvBindByName
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.services.csv.Exportable

/****
 * Author : Ch8n
 * Created-on : 10-02-2021
 */

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
) : Exportable

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
