package com.example.roomateapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.roomateapp.grocery.GroceryManagerActivity

class MainActivity: Activity() {

    //TODO: Implement/Create share room code button? Can be added near logout. Might need to shift those buttons

    private lateinit var groceryButton: Button
    private lateinit var choresButton: Button
    private lateinit var logoutButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        Log.i(TAG, "Entered Main Activity.")

        groceryButton = findViewById<Button>(R.id.grocery_button)
        choresButton = findViewById<Button>(R.id.chores_button)
        logoutButton = findViewById<Button>(R.id.logout_button)

        groceryButton.setOnClickListener {
            Log.i(TAG, "Selected Grocery List from Main Activity.")

            var intent: Intent = Intent(this, GroceryManagerActivity::class.java)
            startActivity(intent)
        }

        choresButton.setOnClickListener {
            Log.i(TAG, "Selected Grocery List from Main Activity.")

            var intent: Intent = Intent(this, ChoresManagerActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            Log.i(TAG, "Selected logout from Main Activity")

            //TODO: Implement logout, probably by deleting the record from the SharedPreferences
            // then finding a way to start the activity from the logout without being able to back button in
        }
    }

    companion object {
        private const val TAG = "RoommateApp"
    }
}