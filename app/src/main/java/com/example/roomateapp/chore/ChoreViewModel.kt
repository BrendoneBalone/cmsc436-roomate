package com.example.roomateapp.chore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.response.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class ChoreViewModel : ViewModel(){

    private val _roomcode = MutableLiveData<String>()

    private val _choreSuccess = MutableLiveData<Boolean>()

    /** Immutable LiveData for external publishing/observing. */
    val roomcode: LiveData<String>
        get() = _roomcode
    val choreSuccess: LiveData<Boolean>
        get() = _choreSuccess
    val gson = GsonBuilder().setPrettyPrinting().create()
    var job: Job? = null

    fun onChoreCreated(roomcode: String, username: String, chore: String, completed: Boolean, date: String) {


        job = viewModelScope.launch {
            try {
                // 1. Run the suspending network request.
                    Log.i(TAG,roomcode)
                val response = makePostRequest("$URL/chores/roomcode/$roomcode/name/$chore")

                if (response.status.value == 200) {
                    _choreSuccess.postValue(true)
                    Log.i(TAG, "Received 200")
                }

            } catch (e: Exception) {
                // Something went wrong ... post error to LiveData feed.
                _roomcode.postValue("Network request failed: ${e.message}")
            }
        }

    }



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

    private fun String.prettyPrint(): String =
        gson.toJson(JsonParser.parseString(this))

    private suspend fun makeGetRequest(url: String): String =
        withContext(Dispatchers.IO) {
            // Construct a new Ktor HttpClient to perform the get
            // request and then return the JSON result.
            HttpClient().get(url)
        }

    private suspend fun makePostRequest(url: String): HttpResponse =
        withContext(Dispatchers.IO) {
            // Construct a new Ktor HttpClient to perform the get
            // request and then return the JSON result.
            HttpClient().post(url)
        }


    // Constants
    companion object {
        private const val TAG = "RoommateApp"

        private const val URL = "http://436env5.eba-vukmr2km.us-east-2.elasticbeanstalk.com"
    }
}