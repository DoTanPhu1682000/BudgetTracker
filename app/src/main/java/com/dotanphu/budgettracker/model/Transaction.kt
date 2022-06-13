package com.dotanphu.budgettracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "_label")
    var label: String,
    @ColumnInfo(name = "_amount")
    var amount: Double,
    @ColumnInfo(name = "_tag")
    var tag: String,
    @ColumnInfo(name = "_date")
    var date: String,
    @ColumnInfo(name = "_description")
    var description: String,
    @ColumnInfo(name = "_type")
    var type: String
) : Serializable {

}