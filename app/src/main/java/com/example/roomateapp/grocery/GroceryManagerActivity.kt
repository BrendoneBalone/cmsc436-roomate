package com.example.roomateapp.grocery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomateapp.MainActivity
import com.example.roomateapp.R

class GroceryManagerActivity : AppCompatActivity() {

    private lateinit var mAdapter: GroceryListAdapter
    private lateinit var mGroceryViewModel: GroceryViewModel

    private var roomcode: String? = null
    private var username: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grocery_recycle_view)

        supportActionBar?.hide()

        roomcode = intent.getStringExtra("roomcode")
        username = intent.getStringExtra("username")

        Log.i(TAG, "Registered roomcode of $roomcode and username of $username in MainActivity")
        Log.i(TAG, "Intent looks as follows: ${intent.extras.toString()}")

        val mRecyclerView = findViewById<RecyclerView>(R.id.list)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter = GroceryListAdapter(this)
        mGroceryViewModel = ViewModelProvider(this)[GroceryViewModel::class.java]

        mRecyclerView.adapter = mAdapter

        mGroceryViewModel.getGroceryList(roomcode!!)

        mGroceryViewModel.groceries.observe(this) { result ->
            Log.i(TAG, "GroceryManagerActivity has registered a database pull of:\n" +
                    "$result")

            if(result.containsKey("status")) {
                throw Error("Unable to request Groceries from database. Check logs.")
            }

            for(key in result.keys) {
                Log.i(TAG, "Adding $key to the list")
                mAdapter.add(GroceryItem(key))
            }

            Log.i(TAG, "Groceries should be added.")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i(TAG, "Entered onActivityResult() in GroceryManagerAdapter")

        if(requestCode == ADD_GROCERY_ITEM_REQUEST && resultCode == RESULT_OK) {
            mAdapter.add(GroceryItem(data!!))
            mGroceryViewModel.addGroceryItem(roomcode!!, data!!.getStringExtra("name")!!)
        }
    }

    public override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        updateDatabase()
    }

    /**
     * Deletes items that need to be removed from the database
     */
    private fun updateDatabase() {
        var items: ArrayList<String> = mAdapter.toDelete

        for(itemName: String in items) {
            mGroceryViewModel.deleteGroceryItem(roomcode!!, itemName)
            mAdapter.delete(itemName)
        }

        mAdapter.toDelete.clear()
    }

    companion object {
        private const val TAG = "RoommateApp"
        const val ADD_GROCERY_ITEM_REQUEST = 0
    }
}