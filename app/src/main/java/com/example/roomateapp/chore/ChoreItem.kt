package com.example.roomateapp.chore

import java.text.ParseException
import java.text.SimpleDateFormat

import android.content.Intent
import java.util.*

class ChoreItem {

    var title : String? = String()
    var status = Status.NOTDONE
    var date : Int = 0
    var assigned : String? = String()
    enum class Status {
        NOTDONE, DONE
    }

    internal constructor(title: String, status: Status, date: Int, assigned: String) {
        this.title = title
        this.status = status
        this.date = date
        this.assigned = assigned
    }

    // Create a new ToDoItem from data packaged in an Intent


    override fun toString(): String {
        return (title + ITEM_SEP + ITEM_SEP + status + ITEM_SEP
                + FORMAT.format(date))
    }

    fun toLog(): String {
        return ("Title:" + title + ITEM_SEP
                + ITEM_SEP + "Status:" + status + ITEM_SEP + "Date:"
                + FORMAT.format(date) + "\n")
    }

    companion object {

        val ITEM_SEP: String? = System.getProperty("line.separator")

        const val TITLE = "title"
        const val STATUS = "status"
        const val DATE = "date"
//        val FILENAME = "filename"

        val FORMAT = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US)

    }

}
