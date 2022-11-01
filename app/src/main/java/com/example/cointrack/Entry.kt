package com.example.cointrack

// Entry:
//- type: string "income"/"expense"
//- name: string
//- date: datetime
//- category: string
//- sum: int
//- details: string

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.time.OffsetDateTime
import java.util.Date

@Entity(tableName = "entriesTable")
class Entry(
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "sum") val sum: Int,
    @ColumnInfo(name = "details") val details: String,
) {
    @PrimaryKey(autoGenerate = true) var id = 0
}