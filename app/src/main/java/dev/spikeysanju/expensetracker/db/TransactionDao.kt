package dev.spikeysanju.expensetracker.db

import androidx.room.*
import dev.spikeysanju.expensetracker.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    // used to insert new transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    // used to update existing transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTransaction(transaction: Transaction)

    // used to delete transaction
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    // get all saved transaction list
    @Query("SELECT * FROM all_transactions")
    fun getAllTransactions(): Flow<List<Transaction>>

    // get all income or expense list
    @Query("SELECT * FROM all_transactions WHERE transactionType == :transactionType")
    fun getTransaction(transactionType: String): Flow<List<Transaction>>

}