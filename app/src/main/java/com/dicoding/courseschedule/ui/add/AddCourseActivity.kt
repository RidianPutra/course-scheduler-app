package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var addCourseName: TextInputEditText
    private lateinit var addDay: Spinner
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private lateinit var addLecturer: TextInputEditText
    private lateinit var addNote: TextInputEditText
    private lateinit var viewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_course)

        addCourseName = findViewById(R.id.add_course_name)
        addDay = findViewById(R.id.add_day)
        tvStartTime = findViewById(R.id.tv_start_time)
        tvEndTime = findViewById(R.id.tv_end_time)
        addLecturer = findViewById(R.id.add_lecturer)
        addNote = findViewById(R.id.add_note)

        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)
        viewModel.saved.observe(this) { event ->
            if (event.getContentIfNotHandled() == true) {
                finish()
            } else {
                Snackbar.make(
                    findViewById(R.id.add_course_activity_layout),
                    getString(R.string.input_empty_message),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_insert -> {
                viewModel.insertCourse(
                    addCourseName.text.toString().trim(),
                    addDay.selectedItemPosition,
                    tvStartTime.text.toString(),
                    tvEndTime.text.toString(),
                    addLecturer.text.toString().trim(),
                    addNote.text.toString().trim()
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        if (tag == "startTimePicker") {
            tvStartTime.text = timeFormat.format(calendar.time)
        } else {
            tvEndTime.text = timeFormat.format(calendar.time)
        }
    }

    fun showStartTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "startTimePicker")
    }

    fun showEndTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "endTimePicker")
    }
}