package com.example.roomateapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity: Activity() {

    //TODO: Add a bit of text specifying username preferences, or adding some more input testing when logging in
    //TODO: Figure out how we're storing the roomcode and user and passing it around as needed. This is urgent and needs to be fixed.

    private lateinit var roomText: EditText
    private lateinit var nameText: EditText
    private lateinit var loginButton: Button
    private lateinit var createRoomButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)
        var roomcode: String? = sharedPreferences.getString(ROOM_PREF_KEY, "")
        var username: String? = sharedPreferences.getString(USER_PREF_KEY, "")

        if(roomcode!!.isEmpty()) {
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
    }

    private fun onLoginClick() {
        var roomcode = roomText.text.toString().trim().uppercase()
        var username: String = nameText.text.toString().trim().lowercase()

        if(roomcode.isEmpty() || username.isEmpty()) {
            Toast.makeText(this@LoginActivity, R.string.missing_login_field, Toast.LENGTH_SHORT)
            return
        }

        requestRoom(roomcode, username)
    }

    private fun requestRoom(roomcode: String, username: String) {
        // TODO: Figure out how to request room
        // Also, make sure to save the roomcode and username to the sharedpreferences after a successful request
        // Use the keys seen in the onCreate and in the companion object below
    }

    private fun onCreateRoomButtonClick() {
        var alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        var newRoomId: String

        alertBuilder.setTitle("Are you sure you want to create a new household?")
        alertBuilder.setMessage(
            "This will create a new household, with a new set of roommates.\n" +
            "Are you sure you want to proceed?")
        alertBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener {
            dialog, id -> dialog.cancel()
        })
        alertBuilder.setPositiveButton("Proceed", DialogInterface.OnClickListener {
            dialog, id -> newRoomId = getNewRoom()
            finish()
        })

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
        return ""
    }

    companion object {
        private const val preferenceKey = "RoommateAppLoginDetails"
        private const val TAG = "RoommateApp"
        const val ROOM_PREF_KEY = "roomcode"
        const val USER_PREF_KEY = "username"
    }
}