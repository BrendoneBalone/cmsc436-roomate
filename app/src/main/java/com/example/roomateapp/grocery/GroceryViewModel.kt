package com.example.roomateapp.grocery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener


class GroceryViewModel: ViewModel() {

    private val _groceries = MutableLiveData<Map<String, *>>()
    val groceries: LiveData<Map<String, *>>
        get() = _groceries


    var job: Job? = null


    // This was taken from https://stackoverflow.com/questions/44870961/how-to-map-a-json-string-to-kotlin-map
    fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }

    // Returns Map version of grocery list
    fun getGroceryList(roomcode: String) {

        Log.i(TAG, "Making getGroceryList request with roomcode $roomcode")

        var obj: Map<String, *>? = null

        // Launch the request in the background
        job = viewModelScope.launch {
            try {
                val response = getRequest("grocery/roomcode/$roomcode")

                Log.i(TAG, "Recieved a response from our getGroceryList request with a body of:\n" +
                        "$response")

                obj = JSONObject(response).toMap()

                if(obj!!["status"] in 200..209) {
                    throw Exception("Received response with code ${obj!!["status"]}")
                }

                _groceries.postValue(obj!!)

            } catch (e: Exception) {
                Log.i(TAG, "Error in getGroceryList in GroceryViewModel: ${e.message}")
            }
        }

    }

    // Adds given grocery item to database
    fun addGroceryItem(roomcode: String, item: String): Boolean {
        var success: Boolean = false

        Log.i(TAG, "Starting an addGroceryItem request with roomcode $roomcode and item $item")

        job = viewModelScope.launch {
            try {
                val response = postRequest("grocery/roomcode/$roomcode/name/$item")

                Log.i(TAG, "Received a response from addGroceryRequest for $item with code ${response.status.value}")

                if (response.status.value in 200..209) success = true
            } catch (e: Exception) {
                Log.i(TAG, "Error in addGroceryItem in GroceryViewModel: ${e.message}")
            }
        }

        return success
    }

    // Deletes given grocery item from database
    fun deleteGroceryItem(roomcode: String, item: String): Boolean {
        var success: Boolean = false

        Log.i(TAG, "Starting a deleteGroceryItem request with roomcode $roomcode and item $item")

        job = viewModelScope.launch {
            try {
                val response = deleteRequest("grocery/roomcode/$roomcode/name/$item")

                Log.i(TAG, "Received a response from deleteGroceryItem for $item with code ${response.status.value}")

                if(response.status.value in 200..209) success = true
            } catch (e: Exception) {
                Log.i(TAG, "Error in deleteGroceryItem in GroceryViewModel: ${e.message}")
            }
        }

        return success
    }

    // Creates generic getRequest
    private suspend fun getRequest(url: String): String =
        withContext(Dispatchers.IO) {
            HttpClient().get("$SERVER/$url")
        }

    // Creates generic postRequest
    private suspend fun postRequest(url: String): HttpResponse =
        withContext(Dispatchers.IO) {
            HttpClient().post("$SERVER/$url")
        }

    // Creates generic deleteRequest
    private suspend fun deleteRequest(url: String): HttpResponse =
        withContext(Dispatchers.IO) {
            HttpClient().delete("$SERVER/$url")
        }

    companion object {
        private const val TAG = "RoommateApp"
        private const val SERVER: String = "http://436env5.eba-vukmr2km.us-east-2.elasticbeanstalk.com"
    }
}
