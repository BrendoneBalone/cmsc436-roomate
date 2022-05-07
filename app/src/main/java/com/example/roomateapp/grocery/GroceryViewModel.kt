package com.example.roomateapp.grocery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GroceryViewModel: ViewModel() {

    private val _liveData = MutableLiveData<String>()
    val liveData: LiveData<String>
        get() = _liveData

    val gson = GsonBuilder().setPrettyPrinting().create()
    var job: Job? = null

    private fun String.prettyPrint(): String {
        return gson.toJson(JsonParser.parseString(this))
    }

    private fun parseJsonString(data: String?): List<String> {
        val result = ArrayList<String>()

        return result
    }

    fun makeGetRequest(url: String) {
        if(job?.isActive == true) {
            return
        }

        _liveData.postValue("Performing GET request...")

        // Launch the request in the background
        job = viewModelScope.launch {
            try {
                val rawJSON: String = getRequest(url)
                _liveData.postValue(parseJsonString(rawJSON))
            } catch (e: Exception) {
                _liveData.postValue("Network request failed: ${e.message}")
            }
        }
    }

    private suspend fun getRequest(url: String): String =
        withContext(Dispatchers.IO) {
            // Construct a new Ktor HttpClient to perform the get
            // request and then return the JSON result.
            HttpClient().get(url)
        }


    companion object {
        private const val TAG = "RoommateApp"
    }
}
