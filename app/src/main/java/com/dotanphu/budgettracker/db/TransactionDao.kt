package com.dotanphu.budgettracker.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dotanphu.budgettracker.model.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Query("DELETE FROM `transaction` WHERE id=:id")
    suspend fun deleterTransaction(id: Int)

    @Query("DELETE FROM `transaction`")
    suspend fun deleterAllTransaction()

    @Query("SELECT * FROM `transaction`")
    fun getAllTransaction(): List<Transaction>

    @Query("SELECT * FROM `transaction` order by id ASC")
    fun getAllTransactionWithLiveData(): LiveData<List<Transaction>>

    @Query("SELECT SUM(_amount) FROM `transaction` WHERE _type = 'Expense' ")
    fun getTotalExpense(): Double

    @Query("SELECT SUM(_amount) FROM `transaction` WHERE _type = 'Income' ")
    fun getTotalIncome(): Double

    @Query("SELECT * FROM `transaction` WHERE _type= :item")
    fun getTransactionsByType(item:String):Flow<List<Transaction>>

    @Query("SELECT *FROM `transaction` " +
            "WHERE _date LIKE '%' || :date || '%'")
    fun getListFollowDate(date: String): List<Transaction>


}