package com.example.cointrack

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class FragmentList : Fragment(), EntryClickInterface, EntryLongClickInterface {

    lateinit var entryRV: RecyclerView
    lateinit var addButton: FloatingActionButton
    lateinit var viewModal: EntryViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_list, container, false)
        // Inflate the layout for this fragment
        entryRV = root.findViewById(R.id.RVEntryList)
        addButton = root.findViewById(R.id.buttonAddEntry)
        entryRV.layoutManager = LinearLayoutManager(this.context)

        val entryRVAdapter = EntryRVAdapter(this.requireContext(), this, this)
        entryRV.adapter = entryRVAdapter
        viewModal = ViewModelProvider(this).get(EntryViewModal::class.java)
        viewModal.allEntries.observe(viewLifecycleOwner, Observer{list ->
            list?.let{
                entryRVAdapter.updateList(it)
            }
        })
        addButton.setOnClickListener{
            val intent = Intent(this.context, AddEditEntryActivity::class.java)
            this.startActivity(intent)
        }
        return root
    }

    override fun onEntryClick(entry: Entry) {
        val intent = Intent(this.context, AddEditEntryActivity::class.java)
        intent.putExtra("type", "Edit")
        intent.putExtra("entryType", entry.type)
        intent.putExtra("entryTitle", entry.name)
        intent.putExtra("entryDate", entry.date)
        intent.putExtra("entryCategory", entry.category)
        intent.putExtra("entrySum", entry.sum.toString())
        intent.putExtra("entryDetails", entry.details)
        intent.putExtra("entryId", entry.id)
        startActivity(intent)
    }

    override fun onEntryLongClick(entry: Entry) {
        viewModal.deleteEntry(entry)
        Toast.makeText(this.context, "${entry.name} Deleted", Toast.LENGTH_LONG).show()
    }

}