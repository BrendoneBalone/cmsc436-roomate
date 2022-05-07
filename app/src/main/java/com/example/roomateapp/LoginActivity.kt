package com.example.roomateapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class LoginActivity: AppCompatActivity() {

    //TODO: Add a bit of text specifying username preferences, or adding some more input testing when logging in
    //TODO: Figure out how we're storing the roomcode and user and passing it around as needed. This is urgent and needs to be fixed.

    private lateinit var roomText: EditText
    private lateinit var nameText: EditText
    private lateinit var loginButton: Button
    private lateinit var createRoomButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var loginViewModel: LoginViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        var roomcode: String? = sharedPreferences.getString(ROOM_KEY, "")
        var username: String? = sharedPreferences.getString(USER_KEY, "")

        Log.i(TAG, "Pulled sharedPreferences, Roomcode of \"$roomcode\", and username of \"$username\"")

        if(roomcode!! != "") {
            setContentView(R.layout.login_loading)
            requestRoom(roomcode, username!!)
        } else {
            setContentView(R.layout.login_activity)

            roomText = findViewById<EditText>(R.id.roomcode)
            nameText = findViewById<EditText>(R.id.name)
            loginButton = findViewById<Button>(R.id.login)
            createRoomButton = findViewById<Button>(R.id.create_room)

            loginButton.setOnClickListener{onLoginClick()}
            createRoomButton.setOnClickListener{onCreateRoomButtonClick()}
        }

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        loginViewModel.roomcode.observe(this) { result ->
            // Update the text view to display the JSON result.
            roomText.setText(result)
        }
        loginViewModel.loginSuccess.observe(this) { result ->
            // Update the text view to display the JSON result.
            Log.i(TAG, "Request room success")
            var transaction: SharedPreferences.Editor = sharedPreferences.edit()
            transaction.putString(ROOM_KEY, roomcode)
            transaction.putString(USER_KEY, username)
            transaction.apply()

            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra(ROOM_KEY, roomcode)
            intent.putExtra(USER_KEY, username)
            startActivity(intent)
            finish()
        }
    }

    private fun onLoginClick() {
        Log.i(TAG, "Login button clicked in LoginActivity")

        var roomcode = roomText.text.toString().trim().uppercase()
        var username: String = nameText.text.toString().trim().lowercase()

        if(roomcode.isEmpty() || username.isEmpty()) {
            Toast.makeText(this@LoginActivity, R.string.missing_login_field, Toast.LENGTH_SHORT).show()
            return
        }

        requestRoom(roomcode, username)
    }

    private fun requestRoom(roomcode: String, username: String) {
        // TODO: Figure out how to request room
        // Also, make sure to save the roomcode and username to the sharedpreferences after a successful request
        // Use the keys seen in the onCreate and in the companion object below
        loginViewModel.onRequestRoomCalled(roomcode, username)
//        if (loginViewModel.onRequestRoomCalled(roomcode, username)) {
//            Log.i(TAG, "Request room success")
//            var transaction: SharedPreferences.Editor = sharedPreferences.edit()
//            transaction.putString(ROOM_KEY, roomcode)
//            transaction.putString(USER_KEY, username)
//            transaction.apply()
//
//            var intent = Intent(this, MainActivity::class.java)
//            intent.putExtra(ROOM_KEY, roomcode)
//            intent.putExtra(USER_KEY, username)
//            startActivity(intent)
//            finish()
//        }
    }

    private fun onCreateRoomButtonClick() {
        Log.i(TAG, "Create Room button clicked in LoginActivity")

        var alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        var newRoomId: String

        alertBuilder.setTitle("Are you sure you want to create a new household?")
        alertBuilder.setMessage(
            "This will create a new household, with a new set of roommates.\n" +
            "Are you sure you want to proceed?")
        alertBuilder.setNegativeButton("Cancel") { dialog, id ->
            dialog.cancel()
        }
        alertBuilder.setPositiveButton("Proceed") { dialog, id ->
            getNewRoom()
        }

        var alert = alertBuilder.create()
        alert.show()

        /* The plan here is to request a new room, then fill out the roomcode EditText box
        * with the new roomcode. This will allow the login process to work the same as it
        * normally would for any other user. We can add a button to maybe copy the roomcode
        * or something of the like on the main page for sharing? Either way I think that will work.
        * Either way, the getNewRoom() call on the AlertDialog positive (line 78) will get the code
        * then down here we will set the EditText to that code, maybe pop a toast message
        * saying that the room was created. Let me know if you have any questions or what you think */
    }

    // This will return the String of the new roomcode
    private fun getNewRoom(): String {
        //TODO: Implement New Room Activity
        loginViewModel.onGetNewRoomCalled()
        return ""
    }

    companion object {
        private const val TAG = "RoommateApp"
        const val ROOM_KEY = "roomcode"
        const val USER_KEY = "username"
        const val PREF_KEY = "RoommateAppLoginDetails"

        private const val URL = "http://10.0.2.2:5000"
    }
}