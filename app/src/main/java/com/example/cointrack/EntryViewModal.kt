package com.example.cointrack

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EntryViewModal(application: Application): AndroidViewModel(application) {
    val allEntries: LiveData<List<Entry>>
    val repository: EntryRepository

    init {
        val dao = EntryDatabase.getDatabase(application).getEntryDao()
        repository = EntryRepository(dao)
        allEntries = repository.allEntries
    }

    fun deleteEntry(entry: Entry) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(entry)
    }

    fun updateEntry(entry: Entry) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(entry)
    }

    fun addEntry(entry: Entry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entry)
    }
}