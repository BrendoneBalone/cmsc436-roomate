package com.example.roomateapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.roomateapp.grocery.GroceryManagerActivity

class MainActivity: Activity() {

    private lateinit var groceryButton: Button
    private lateinit var choresButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        Log.i(TAG, "Entered Main Activity.")

        groceryButton = findViewById<Button>(R.id.grocery_button)
        choresButton = findViewById<Button>(R.id.chores_button)

        groceryButton.setOnClickListener {
            Log.i(TAG, "Selected Grocery List from Main Activity.")

            var intent: Intent = Intent(this, GroceryManagerActivity::class.java)
            startActivity(intent)
        }

        choresButton.setOnClickListener {
            Log.i(TAG, "Selected Grocery List from Main Activity.")

            var intent: Intent = Intent(this, ChoresManagerActivity::class.java)
            startActivity(intent)
        }    }

    companion object {
        private const val TAG = "RoommateApp"
    }
}