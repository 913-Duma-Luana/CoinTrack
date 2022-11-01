package com.example.cointrack

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class AddEditEntryActivity : AppCompatActivity(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener  {
    lateinit var entryTitleEdit : EditText
    lateinit var entrySumEdit : EditText
    lateinit var entryDetailsEdit : EditText
    lateinit var updateButton: Button
    lateinit var viewmodal: EntryViewModal
    lateinit var pickDateButton: Button
    lateinit var dateShow: TextView
    lateinit var pickedCategory: String
    lateinit var pickedType: String
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    var entryId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_entry)
        entryTitleEdit = findViewById(R.id.editEntryTitle)
        entrySumEdit = findViewById(R.id.editEntrySum)
        entryDetailsEdit = findViewById(R.id.editEntryDetails)
        updateButton = findViewById(R.id.updateButton)
        pickDateButton = findViewById(R.id.datePickerButton)
        dateShow = findViewById(R.id.dateShow)
        val categories = resources.getStringArray(R.array.Categories)
        pickDate()

        // Type Tab Picker
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = PagerAdapter(this)
        pickedType = "expense"
        TabLayoutMediator(tabLayout, viewPager) { tab, index ->
            tab.text = when(index){
                0 -> {"expense"}
                1 -> {"income"}
                else -> { throw Resources.NotFoundException("Position not found") }
            }
        }.attach()
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pickedType = tab.text.toString()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) { pickedType = "expense" }
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // SPINNER
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                R.layout.spinner_item, categories)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                pickedCategory = categories[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                pickedCategory = "Others"
            }
        }

        viewmodal = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application = Application()))
            .get(EntryViewModal::class.java)
        val entryType = intent.getStringExtra("type")
        if(entryType.equals("Edit")){
            val title = intent.getStringExtra("entryTitle")
            val type = intent.getStringExtra("entryType")
            val date = intent.getStringExtra("entryDate")
            val category = intent.getStringExtra("entryCategory")
            val sum = intent.getStringExtra("entrySum")
            val details = intent.getStringExtra("entryDetails")
            entryId = intent.getIntExtra("entryId", -1)
            updateButton.setText("Update Entry")
            entryTitleEdit.setText(title)
            entrySumEdit.setText(sum)
            entryDetailsEdit.setText(details)
            dateShow.setText(date)
            if (category == null) {
                pickedCategory = "Others"
                Toast.makeText(this, "NULL HERE", Toast.LENGTH_SHORT).show()
            }
            pickedCategory = category ?: "Others"
            pickedType = type ?: "expense"
            spinner.setSelection(categories.indexOf(pickedCategory))
            var someIndex = 0
            if (pickedType == "income") {someIndex = 1}
            val tab = tabLayout.getTabAt(someIndex)
            tab!!.select()
        }
        else{
            updateButton.setText("Save New Entry")
        }
        updateButton.setOnClickListener{
            val title = entryTitleEdit.text.toString()
            val type = pickedType
            val category = pickedCategory
            val details = entryDetailsEdit.text.toString()
            val date = dateShow.text.toString()
            var sum = 0
            try{
                sum = entrySumEdit.text.toString().toInt()
            } catch (_: NumberFormatException){}

            if(entryType.equals("Edit")){
                if(title.isNotEmpty() && type.isNotEmpty() && date.isNotEmpty() && sum != 0 && category.isNotEmpty()){
                    val updateEntry = Entry(type,title,date,category,sum,details)
                    updateEntry.id = entryId
                    viewmodal.updateEntry(updateEntry)
                    Toast.makeText(this, "Entry updated", Toast.LENGTH_LONG).show()
                } else Toast.makeText(this, "ERROR: Incomplete data. Entry not updated", Toast.LENGTH_LONG).show()
            } else {
                if(title.isNotEmpty() && type.isNotEmpty() && date.isNotEmpty() && sum != 0 && category.isNotEmpty()){
                    viewmodal.addEntry(Entry(type,title,date,category,sum,details))
                    Toast.makeText(this, "Entry added", Toast.LENGTH_LONG).show()
                } else Toast.makeText(this, "ERROR: Incomplete data. Entry not added", Toast.LENGTH_LONG).show()
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {
        pickDateButton.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(this,this,year,month,day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        savedDay = day
        savedMonth = month
        savedYear = year
        getDateTimeCalendar()
        TimePickerDialog(this,this,hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        savedHour = hour
        savedMinute = minute
        if(savedHour<10 && savedMinute<10) dateShow.text = "$savedDay-$savedMonth-$savedYear\n 0$savedHour:0$savedMinute"
        else if(savedHour<10) dateShow.text = "$savedDay-$savedMonth-$savedYear\n 0$savedHour:$savedMinute"
        else if(savedMinute<10) dateShow.text = "$savedDay-$savedMonth-$savedYear\n $savedHour:0$savedMinute"
        else dateShow.text = "$savedDay-$savedMonth-$savedYear\n $savedHour:$savedMinute"



    }
}