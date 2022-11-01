package com.example.cointrack

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EntryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entry: Entry)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(entry: Entry)

    @Delete
    fun delete(entry: Entry)

    @Query("Select * from entriesTable order by datetime(date) ASC")
    fun getAll(): LiveData<List<Entry>>

}