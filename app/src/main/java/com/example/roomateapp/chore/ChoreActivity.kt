package com.example.roomateapp.chore

import java.util.Calendar
import java.util.Date

import android.app.DatePickerDialog
import android.app.Dialog
import androidx.fragment.app.DialogFragment
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.TimePicker
import com.example.roomateapp.R
import androidx.fragment.app.FragmentActivity
import com.example.roomateapp.chore.ChoreItem.Status

class ChoreActivity : FragmentActivity() {

    private lateinit var mDate: Date
    private lateinit var mStatusRadioGroup: RadioGroup
    private lateinit var mTitleText: EditText
    private lateinit var dateView: TextView

    private var roomcode: String? = null
    private var username: String? = null

    private val status: Status
        get() {

          return  Status.NOTDONE
        }


    private val weekday = arrayOf(" ","SUN","MON","TUE","WED","THU","FRI","SAT")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_chore)
        roomcode = intent.getStringExtra("roomcode")
        username = intent.getStringExtra("username")

        mTitleText = findViewById<View>(R.id.chore_title) as EditText
        dateView = findViewById<View>(R.id.date) as TextView

        // Set the default date and time

        setDefaultDateTime()

        // OnClickListener for the Date button, calls showDatePickerDialog() to
        // show
        // the Date dialog

        val datePickerButton = findViewById<View>(R.id.date_picker_button) as Button
        datePickerButton.setOnClickListener { showDatePickerDialog() }

        // OnClickListener for the Time button, calls showTimePickerDialog() to
        // show
        // the Time Dialog


        // OnClickListener for the Cancel Button,

        val cancelButton = findViewById<View>(R.id.chore_cancel_button) as Button
        cancelButton.setOnClickListener {
            Log.i(TAG, "Entered cancelButton.OnClickListener.onClick()")

            //Indicate result and finish
            setResult(RESULT_CANCELED)
            finish()
        }

        //Set up OnClickListener for the Reset Button
        val resetButton = findViewById<View>(R.id.chore_resetButton) as Button
        resetButton.setOnClickListener {
            Log.i(TAG, "Entered resetButton.OnClickListener.onClick()")

            //Reset data to default values
            setDefaultDateTime()
            mTitleText.text = null
        }

        // Set up OnClickListener for the Submit Button

        val submitButton = findViewById<View>(R.id.chore_submit_button) as Button
        submitButton.setOnClickListener {
            Log.i(TAG, "Entered submitButton.OnClickListener.onClick()")

            // Get Status

            // Title
            val titleData = mTitleText.text.toString()
            // Date

            val dateData = dateView.text.toString()

            // Package ToDoItem data into an Intent
            val dataIntent: Intent = Intent().apply {
                putExtra("title",titleData)
                putExtra("status",Status.NOTDONE)
                putExtra("date",dateData)
                putExtra("roomcode", roomcode)
                putExtra("username", username)
            }

            // TODO - return data Intent and finish
            setResult(RESULT_OK,dataIntent)
            finish()
        }
    }

    private fun setDefaultDateTime() {

        // Default is current time
        mDate = Date()
        mDate = Date(mDate.time)

        val c = Calendar.getInstance()
        c.time = mDate
        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH))

        dateView.text = weekday[c.get(Calendar.DAY_OF_WEEK)]

    }

    // DialogFragment used to pick a ToDoItem deadline date

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
        private val weekday = arrayOf(" ","SUN","MON","TUE","WED","THU","FRI","SAT")
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(requireActivity(), this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                               dayOfMonth: Int) {
            setDateString(year, monthOfYear, dayOfMonth)
            val dateView: TextView = requireActivity().findViewById(R.id.date)
            val c = Calendar.getInstance()
            c.set(year,monthOfYear,dayOfMonth)
            dateView.text = weekday[c.get(Calendar.DAY_OF_WEEK)]
        }
    }

    // DialogFragment used to pick a ToDoItem deadline time

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }



    companion object {

        // 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
        private const val SEVEN_DAYS = 604800000
        private val weekday = arrayOf(" ","SUN","MON","TUE","WED","THU","FRI","SAT")
        private const val TAG = "RoommateApp"

        private var timeString: String? = null
        private var dateString: String? = null

        private fun setDateString(year: Int, monthOfYear: Int, dayOfMonth: Int) {
            var localMonthOfYear = monthOfYear

            // Increment monthOfYear for Calendar/Date -> Time Format setting
            localMonthOfYear++
            var mon = "" + localMonthOfYear
            var day = "" + dayOfMonth

            if (localMonthOfYear < 10)
                mon = "0$localMonthOfYear"
            if (dayOfMonth < 10)
                day = "0$dayOfMonth"

            dateString = "$year-$mon-$day"
        }

        private fun setTimeString(hourOfDay: Int, minute: Int) {
            var hour = "" + hourOfDay
            var min = "" + minute

            if (hourOfDay < 10)
                hour = "0$hourOfDay"
            if (minute < 10)
                min = "0$minute"

            timeString = "$hour:$min:00"
        }
    }
}
