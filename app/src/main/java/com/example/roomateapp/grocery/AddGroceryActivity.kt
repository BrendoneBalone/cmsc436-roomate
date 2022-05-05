package com.example.roomateapp.grocery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.example.roomateapp.R

class AddGroceryActivity : FragmentActivity() {

    private lateinit var name: EditText
    private lateinit var cancelButton: Button
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)

        name = findViewById<EditText>(R.id.title)
        cancelButton = findViewById<Button>(R.id.cancel_button)
        submitButton = findViewById<Button>(R.id.submit_button)

        cancelButton.setOnClickListener {
            Log.i(TAG, "Entered cancelButton's onClick")

            setResult(RESULT_CANCELED, Intent())
            finish()
        }

        submitButton.setOnClickListener {
            Log.i(TAG, "Entered submitButton's onClick")

            val name: String = name.text.toString()
            Log.i(TAG, "The new item has a name of \"$name\".")

            val intent = Intent()
            intent.putExtra("name", name)

            setResult(RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        private const val TAG = "RoommateApp"
    }
}