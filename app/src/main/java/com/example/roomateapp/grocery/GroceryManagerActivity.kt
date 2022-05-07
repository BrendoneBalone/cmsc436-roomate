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
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomateapp.LoginActivity
import com.example.roomateapp.MainActivity
import com.example.roomateapp.R

class GroceryManagerActivity : AppCompatActivity() {

    private lateinit var mAdapter: GroceryListAdapter
    private lateinit var mGroceryViewModel: GroceryViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grocery_recycle_view)

        val mRecyclerView = findViewById<RecyclerView>(R.id.list)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter = GroceryListAdapter(this)
        mGroceryViewModel = ViewModelProvider(this)[GroceryViewModel::class.java]

        mRecyclerView.adapter = mAdapter

        importGroceryList()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i(TAG, "Entered onActivityResult() in GroceryManagerAdapter")

        if(requestCode == ADD_GROCERY_ITEM_REQUEST && resultCode == RESULT_OK) {
            mAdapter.add(GroceryItem(data!!))
        }

        var itemName: String? = data!!.getStringExtra("name")
        mGroceryViewModel.addGroceryItem(MainActivity.roomcode!!, itemName!!)
    }

    public override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        updateDatabase()
    }

    /**
     * Imports all groceries from the given database
     */
    private fun importGroceryList() {
        var groceries: Map<String, *>? = mGroceryViewModel.getGroceryList(MainActivity.roomcode!!)

        if(!groceries.isNullOrEmpty()) {
            for (key in groceries!!.keys) {
                mAdapter.add(GroceryItem(key))
            }
        }
    }

    /**
     * Deletes items that need to be removed from the database
     */
    private fun updateDatabase() {
        var items: ArrayList<String> = mAdapter.toDelete

        for(itemName: String in items) {
            mGroceryViewModel.deleteGroceryItem(MainActivity.roomcode!!, itemName)
            mAdapter.delete(itemName)
        }

        mAdapter.toDelete.clear()
    }

    companion object {
        private const val TAG = "RoommateApp"
        const val ADD_GROCERY_ITEM_REQUEST = 0
        private const val MENU_ALL = Menu.FIRST

        const val host: String = "http://localhost:5000"
    }
}