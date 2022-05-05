package com.example.roomateapp.grocery

import android.content.Intent

class GroceryItem {

    var name: String? = String()
    var status = Status.NOTDONE

    enum class Status {
        NOTDONE, DONE
    }


    // Creates GroceryItem from list of properties
    internal constructor(name: String) {
        this.name = name
        this.status = Status.NOTDONE
    }


    // Creates GroceryItems from packaged intent
    internal constructor(intent: Intent) {
        name = intent.getStringExtra("name")
        status = Status.NOTDONE
    }

    override fun toString(): String {
        return ("$name, $status")
    }

    fun toLog(): String {
        return ("$name, $status")
    }

    companion object {
        private const val TAG = "RoommateApp"
    }

}