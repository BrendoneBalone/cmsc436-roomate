package com.example.roomateapp.grocery

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class GroceryManagerActivity : Activity() {

    private lateinit var mAdapter: GroceryListAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                //TODO: Implement check all
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
        private const val MENU_ALL = Menu.FIRST
    }
}