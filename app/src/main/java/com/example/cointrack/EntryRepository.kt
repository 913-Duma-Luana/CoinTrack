package com.example.cointrack

import androidx.lifecycle.LiveData

class EntryRepository(private val entryDao: EntryDao) {
    val allEntries: LiveData<List<Entry>> = entryDao.getAll()

    suspend fun insert(entry: Entry){
        entryDao.insert(entry)
    }

    suspend fun delete(entry: Entry){
        entryDao.delete(entry)
    }

    suspend fun update(entry: Entry){
        entryDao.update(entry)
    }
}