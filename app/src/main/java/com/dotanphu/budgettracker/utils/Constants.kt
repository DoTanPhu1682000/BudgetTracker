package com.dotanphu.budgettracker.utils

object Constants {
    val transactionTags = listOf(
        "Transportation",
        "Food",
        "Healthcare",
        "Home",
    )

    val transactionType = listOf(
        "Income",
        "Expense"
    )

    fun transactionType():ArrayList<String>{
        val list= ArrayList<String>()
        list.add("Income")
        list.add("Expense")
        return list
    }
}