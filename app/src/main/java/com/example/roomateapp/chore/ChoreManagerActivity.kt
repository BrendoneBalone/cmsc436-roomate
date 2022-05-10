package com.example.roomateapp.chore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomateapp.R
import com.example.roomateapp.chore.ChoreItem.Status
import java.util.*
import kotlin.collections.ArrayList

class ChoreManagerActivity : AppCompatActivity() {

    private lateinit var mAdapter: ChoreListAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var choreViewModel: ChoreViewModel
    private lateinit var mAddChoreViewModel: AddChoreViewModel
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
            var assigned : String? = null
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
                        if (key2 == "username") {
                            assigned = value2.toString()

                        }

                    }

                }

                mAdapter.add(ChoreItem(key,completed,weekday.indexOf(day),assigned!!))
            }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i(TAG, "Entered onActivityResult()")
        val newTitle:String
        val status:Status
        val dataBaseStatus : Boolean
        val date: Int
        val mChoreItem: ChoreItem
        val assigned :String
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
                dataBaseStatus = when(data.getStringExtra("status").toString()) {
                    "DONE" -> true
                    else -> {
                        false
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

                assigned = data.getStringExtra("assigned").toString()

                mChoreItem = ChoreItem(newTitle, status, date,assigned )
                mAdapter.add(mChoreItem)
                choreViewModel.onChoreCreated(intent.getStringExtra("roomcode")!!,mChoreItem.assigned.toString(),mChoreItem.title.toString(),dataBaseStatus,weekday[mChoreItem.date])
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
        updateDatabase()
    }

    override fun onStop() {
        super.onStop()

    }

    private fun updateDatabase() {
        var items: ArrayList<String> = mAdapter.toDelete
        var completed : ArrayList<String> = mAdapter.toComplete
        var unCompleted: ArrayList<String> = mAdapter.toUnComplete
        for (item in completed) {
            Log.i(TAG,item)
            choreViewModel.completeChore(roomcode!!,item)
        }
        for (item in unCompleted) {
            Log.i(TAG,item)
            choreViewModel.unCompleteChore(roomcode!!,item)
        }
        for(itemName: String in items) {
            choreViewModel.deleteChore(roomcode!!, itemName)
        }

        mAdapter.toDelete.clear()
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


    companion object {

        const val ADD_TODO_ITEM_REQUEST = 0
        const val TAG = "RoommateApp"

        // IDs for menu items
        private const val MENU_DELETE = Menu.FIRST
    }
}