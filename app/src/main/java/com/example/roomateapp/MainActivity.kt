package com.example.roomateapp

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.roomateapp.chore.ChoreManagerActivity
import com.example.roomateapp.grocery.GroceryManagerActivity

class MainActivity: Activity() {

    private lateinit var groceryButton: Button
    private lateinit var choresButton: Button
    private lateinit var logoutButton: Button
    private lateinit var copyButton: Button

    private var roomcode: String? = null
    private var username: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        Log.i(TAG, "Entered Main Activity.")

        roomcode = intent.getStringExtra("roomcode")
        username = intent.getStringExtra("username")

        Log.i(TAG, "Registered roomcode of $roomcode and username of $username in MainActivity")

        groceryButton = findViewById<Button>(R.id.grocery_button)
        choresButton = findViewById<Button>(R.id.chores_button)
        logoutButton = findViewById<Button>(R.id.logout_button)
        copyButton = findViewById<Button>(R.id.copy_button)

        groceryButton.setOnClickListener {
            Log.i(TAG, "Selected Grocery List from Main Activity.")

            var intent: Intent = Intent(this, GroceryManagerActivity::class.java)
            intent.putExtra("roomcode", roomcode)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        choresButton.setOnClickListener {
            Log.i(TAG, "Selected chore from Main Activity.")
            val intent = Intent(this, ChoreManagerActivity::class.java)
            intent.putExtra("roomcode",roomcode)
            intent.putExtra("username",username)

            //var intent: Intent = Intent(this, ChoresManagerActivity::class.java)

            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            Log.i(TAG, "Selected logout from Main Activity")

            roomcode = null
            username = null

            var sharedPreferences = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("username").remove("roomcode").apply()

            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        copyButton.setOnClickListener {
            Log.i(TAG, "Selected copy from Main Activity")

            val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("roomcode", roomcode)
            clipboardManager.setPrimaryClip(clipData)

            Toast.makeText(this, "Copied roomcode to clipboard", Toast.LENGTH_SHORT).show()
        }


    }

    companion object {
        private const val TAG = "RoommateApp"
        const val PREF_KEY = "RoommateAppLoginDetails"
    }
}