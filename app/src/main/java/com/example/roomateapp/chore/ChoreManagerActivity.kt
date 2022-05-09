package com.example.roomateapp.chore

import android.app.Activity
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.text.ParseException

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomateapp.LoginViewModel
import com.example.roomateapp.R
import com.example.roomateapp.chore.ChoreListAdapter
import com.example.roomateapp.chore.ChoreItem.Status
import com.example.roomateapp.grocery.GroceryItem
import com.example.roomateapp.grocery.GroceryManagerActivity
import java.sql.Time
import java.util.*

class ChoreManagerActivity : AppCompatActivity() {

    private lateinit var mAdapter: ChoreListAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var choreViewModel: ChoreViewModel
    private var roomcode: String? = null
    private var username: String? = null
    private val weekday = arrayOf(" ","sunday","monday","tuesday","wednesday","thursday","friday","saturday")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chore_recycle_view)
        Log.i(TAG, "Entered onCreate()")
        roomcode = intent.getStringExtra("roomcode")
        username = intent.getStringExtra("username")
        //Create a new TodoListAdapter for this Activity's RecyclerView
        mAdapter = ChoreListAdapter(this)
        mRecyclerView = findViewById<RecyclerView>(R.id.chore_list)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        choreViewModel = ViewModelProvider(this)[ChoreViewModel::class.java]


        //Attach the adapter to this Activity's RecyclerView
        mRecyclerView.adapter = mAdapter

        choreViewModel.getChoreList(roomcode!!)

        choreViewModel.chores.observe(this) {result ->
            if(result.containsKey("status")) {
                throw Error("Unable to request chores from database. Check logs.")
            }
            var day: String? = null
            var completed: Status = Status.DONE
            for((key,value) in result) {
                if (value is Map<*, *>){
                    for ((key2,value2) in value) {
                        if (key2 == "weekday") {
                            day = value2.toString()
                        }
                        if (key2 == "completed") {
                            completed = when (value2.toString()) {
                                "false" -> Status.NOTDONE
                                else -> Status.DONE
                            }
                        }
                    }
                }
                Log.i(TAG,"chore by name of $key")
                mAdapter.add(ChoreItem(key,completed,weekday.indexOf(day)))
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i(TAG, "Entered onActivityResult()")
        val newTitle:String
        val status:Status
        val date: Int
        val mChoreItem: ChoreItem
        val time : Time
        //Check result code and request code
        // if user submitted a new ToDoItem
        // Create a new ToDoItem from the data Intent
        // and then add it to the adapter
        if (resultCode == RESULT_OK && requestCode == ADD_TODO_ITEM_REQUEST) {
            if (data != null) {

                newTitle = data.getStringExtra("title").toString()

                status = when(data.getStringExtra("status").toString()) {
                    "DONE" -> Status.DONE
                    else -> {
                        Status.NOTDONE
                    }
                }

                date = when (data.getStringExtra("date").toString()) {
                    "SUN" -> Calendar.SUNDAY
                    "MON" -> Calendar.MONDAY
                    "TUE" -> Calendar.TUESDAY
                    "WED" -> Calendar.WEDNESDAY
                    "THU" -> Calendar.THURSDAY
                    "FRI" -> Calendar.FRIDAY
                    "SAT" -> Calendar.SATURDAY
                    else -> 0
                }



                Log.i(TAG,"$date")
                mChoreItem = ChoreItem(newTitle, status, date)
                mAdapter.add(mChoreItem)

                choreViewModel.onChoreCreated(intent.getStringExtra("roomcode")!!,intent.getStringExtra("username").toString(),mChoreItem.title.toString(),mChoreItem.status.toString().toBoolean(),weekday[mChoreItem.date])
                Log.i(TAG,"posted chore")
            }

        }
    }

    // Do not modify below here

    public override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all")

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_DELETE -> {
                mAdapter.clear()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    // Load stored ToDoItems
//    private fun loadItemsFromFile() {
//        var reader: BufferedReader? = null
//        try {
//            val fis = openFileInput(FILE_NAME)
//            reader = BufferedReader(InputStreamReader(fis))
//
//            var title: String?
//            var priority: String?
//            var status: String?
//            var date: Date?
//
//            do {
//                title = reader.readLine()
//                if (title == null)
//                    break
//                priority = reader.readLine()
//                status = reader.readLine()
//                date = ChoreItem.FORMAT.parse(reader.readLine())
//                mAdapter.add(ChoreItem(title,
//                        Status.valueOf(status), date))
//
//            }
//            while (true)
//
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        } finally {
//            if (null != reader) {
//                try {
//                    reader.close()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//
//            }
//        }
//    }

//    // Save ToDoItems to file
//    private fun saveItemsToFile() {
//        var writer: PrintWriter? = null
//        try {
//            val fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
//            writer = PrintWriter(BufferedWriter(OutputStreamWriter(
//                    fos)))
//
//            for (idx in 1 until mAdapter.itemCount) {
//
//                writer.println(mAdapter.getItem(idx))
//
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            writer?.close()
//        }
//    }

    companion object {

        const val ADD_TODO_ITEM_REQUEST = 0
        const val TAG = "RoommateApp"

        // IDs for menu items
        private const val MENU_DELETE = Menu.FIRST
    }
}