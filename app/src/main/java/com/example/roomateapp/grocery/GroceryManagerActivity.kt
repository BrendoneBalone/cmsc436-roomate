package com.example.roomateapp.grocery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomateapp.R

class GroceryManagerActivity : Activity() {

    private lateinit var mAdapter: GroceryListAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grocery_recycle_view)

        val mRecyclerView = findViewById<RecyclerView>(R.id.list)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter = GroceryListAdapter(this)

        loadItemsFromDatabase()

        mRecyclerView.adapter = mAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "Entered onActivityResult() in GroceryManagerAdapter")

        if(requestCode == ADD_GROCERY_ITEM_REQUEST && resultCode == RESULT_OK) {
            mAdapter.add(GroceryItem(data!!))
        }
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

    private fun loadItemsFromDatabase() {
        //TODO: Implement pulling and creating objects from the database
    }

    private fun saveGroceryList() {
        //TODO: Implement saving items to database
    }

    companion object {
        private const val TAG = "RoommateApp"
        const val ADD_GROCERY_ITEM_REQUEST = 0
        private const val MENU_ALL = Menu.FIRST
    }
}