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

    private lateinit var roomText: EditText
    private lateinit var nameText: EditText
    private lateinit var loginButton: Button
    private lateinit var createRoomButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)

        if(/* There is a shared preference */) {
            setContentView(/* Login Loading Layout */)
            //TODO: Implement pulling roomcode and user from SharedPreference
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
        var roomcode: String = roomText.text.toString().trim()
        var name: String = nameText.text.toString().trim()

        if(roomcode.isEmpty() || name.isEmpty()) {
            Toast.makeText(this@LoginActivity, R.string.missing_login_field, Toast.LENGTH_SHORT)
            return
        }

        var alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Are you sure you want to login with this information?")
        alertBuilder.setMessage(
            "Your roomcode is set as $roomcode.\n" +
            "Your username is set as $name.\n" +
            "This will permantly assign you this room and username. You cannot change these parameters." +
            "Are you sure you want to proceed?")
        alertBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener {
          dialog, id -> dialog.cancel()
        })
        alertBuilder.setPositiveButton("Login", DialogInterface.OnClickListener {
            dialog, id -> requestRoom(roomcode, name)
        })

        val alert = alertBuilder.create()
        alert.show()

    }

    private fun requestRoom(roomString: String, username: String) {
        //TODO: Figure out how to request room
    }

    private fun onCreateRoomButtonClick() {
        var alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        var newRoomId: Int

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
    }

    private fun getNewRoom(): Int {
        //TODO: Implement New Room Activity
    }

    companion object {
        private const val preferenceKey = "RoommateAppLoginDetails"
        private const val TAG = "RoommateApp"
    }
}