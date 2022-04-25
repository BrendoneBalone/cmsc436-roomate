package com.example.roomateapp.grocery

import android.content.Intent

class GroceryItem {

    // TODO: Create variables for each property of GroceryItem

    enum class Status {
        NOTDONE, DONE
    }


    // Creates GroceryItem from list of properties
    internal constructor(/* Add variables here */) {
        //TODO: Fill once item layout is created
    }


    // Creates GroceryItems from packaged intent
    internal constructor(intent: Intent) {
       //TODO: Fill once item layout is created
    }


    // Package list of variables into an intent
    fun packageIntent(/* Add variables here */) {
        //TODO: Implement based on properties after layout created
    }

    override fun toString(): String {
        //TODO: Implement based on properties after layout created
    }

    fun toLog(): String {
        //TODO: Implement based on properties after layout created
    }

    companion object {
        private const val TAG = "RoommateApp"
    }

}