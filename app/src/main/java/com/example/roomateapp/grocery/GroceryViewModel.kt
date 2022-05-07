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

    private val _liveData = MutableLiveData<Map<String, *>>()
    val liveData: LiveData<Map<String, *>>
        get() = _liveData

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
    fun getGroceryList(roomcode: String): Map<String, *>? {

        var obj: Map<String, *>? = null

        // Launch the request in the background
        job = viewModelScope.launch {
            try {
                val response = getRequest("grocery/roomcode/$roomcode")

                if(response.status.value in 200..209) {
                    val rawJSON: String = response.content.toString()
                    obj = JSONObject(rawJSON).toMap()
                }

                _liveData.postValue(obj!!)

            } catch (e: Exception) {
                Log.i(TAG, "Error in getGroceryList in GroceryViewModel: ${e.message}")
            }
        }

        return obj
    }

    // Adds given grocery item to database
    fun addGroceryItem(roomcode: String, item: String): Boolean {
        var success: Boolean = false

        job = viewModelScope.launch {
            try {
                val response = postRequest("grocery/roomcode/$roomcode/name/$item")
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

        job = viewModelScope.launch {
            try {
                val response = deleteRequest("grocery/roomcode/$roomcode/name/$item")
                if(response.status.value in 200..209) success = true
            } catch (e: Exception) {
                Log.i(TAG, "Error in deleteGroceryItem in GroceryViewModel: ${e.message}")
            }
        }

        return success
    }

    // Creates generic getRequest
    private suspend fun getRequest(url: String): HttpResponse =
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
        private const val SERVER: String = "http://10.0.2.2:5000"
    }
}
