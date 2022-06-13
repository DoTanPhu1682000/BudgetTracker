package com.dotanphu.budgettracker.respository

import androidx.lifecycle.LiveData
import com.dotanphu.budgettracker.db.TransactionDao
import com.dotanphu.budgettracker.model.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRespository(private val transactionDao: TransactionDao) {

    fun getAllTransaction(): LiveData<List<Transaction>> {
        return transactionDao.getAllTransactionWithLiveData()
    }

    suspend fun insert(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun delete(id: Int) {
        transactionDao.deleterTransaction(id)
    }

    suspend fun deleteAllTransaction() {
        transactionDao.deleterAllTransaction()
    }

    fun getTotalIncome() : Double{
        return transactionDao.getTotalIncome()
    }

    fun getTotalExpense() : Double{
        return transactionDao.getTotalExpense()
    }
    fun getFilteredTransactions(item:String):Flow<List<Transaction>> = transactionDao.getTransactionsByType(item)

}