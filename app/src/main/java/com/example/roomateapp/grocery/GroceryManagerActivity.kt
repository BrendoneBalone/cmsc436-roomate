package com.example.roomateapp.grocery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.ktor.client.*
import io.ktor.client.request.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomateapp.MainActivity
import com.example.roomateapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

class GroceryManagerActivity : Activity() {

    private lateinit var mAdapter: GroceryListAdapter

    private val _liveData = MutableLiveData<String>()
    val liveData: LiveData<String>
        get() = _liveData

    val gson = GsonBuilder().setPrettyPrinting().create()

    var job: Job? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grocery_recycle_view)

        val mRecyclerView = findViewById<RecyclerView>(R.id.list)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter = GroceryListAdapter(this)

        var rawJSON: String = getItemsFromDatabase()
        rawJSON = rawJSON.prettyPrint()

        mRecyclerView.adapter = mAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "Entered onActivityResult() in GroceryManagerAdapter")

        if(requestCode == ADD_GROCERY_ITEM_REQUEST && resultCode == RESULT_OK) {
            mAdapter.add(GroceryItem(data!!))
        }

        //TODO: Add new item to the database
    }

    public override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        saveGroceryList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menu.add(Menu.NONE, MENU_ALL, Menu.NONE, "Check all")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_ALL -> {
                mAdapter.clear()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private suspend fun getItemsFromDatabase(): String {
        val requestURL: String = "$host/grocery/roomcode/${MainActivity.roomcode}"

        withContext(Dispatchers.IO) {
            return HttpClient().get(requestURL)
        }
    }

    private fun saveGroceryList() {
        //TODO: Implement saving items to database
    }

    companion object {
        private const val TAG = "RoommateApp"
        const val ADD_GROCERY_ITEM_REQUEST = 0
        private const val MENU_ALL = Menu.FIRST

        const val host: String = "http://localhost:5000"
    }
}