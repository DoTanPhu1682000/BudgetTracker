package com.dotanphu.budgettracker.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.dotanphu.budgettracker.db.TransactionDatabase
import com.dotanphu.budgettracker.model.Transaction
import com.dotanphu.budgettracker.respository.TransactionRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    val repository: TransactionRespository

    var income : MutableLiveData<Double> = MutableLiveData()

    var expense : MutableLiveData<Double> = MutableLiveData()

    init {
        val dao = TransactionDatabase.getInstance(application).getTransactionDao()
        repository = TransactionRespository(dao)
    }

    fun getAllTransaction(): LiveData<List<Transaction>> = repository.getAllTransaction()

    fun insertTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(transaction)
    }

    fun updateTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(transaction)
    }

    fun deleteTransaction(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(id)
    }
    fun deleteAllTransaction() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAllTransaction()
    }

    fun getTotalAmountByType() = viewModelScope.launch(Dispatchers.IO) {
        income.postValue(repository.getTotalIncome())
        Log.d("income", income.value.toString())

        expense.postValue(repository.getTotalExpense())
        Log.d("expense", expense.value.toString())
    }
    fun getFilteredTransactions(item: String): LiveData<List<Transaction>> =
        repository.getFilteredTransactions(item).asLiveData()
}
