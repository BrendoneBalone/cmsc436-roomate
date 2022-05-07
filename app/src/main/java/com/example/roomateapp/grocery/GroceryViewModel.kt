package com.example.roomateapp.grocery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener


class GroceryViewModel: ViewModel() {

    private val _liveData = MutableLiveData<JSONObject>()
    val liveData: LiveData<JSONObject>
        get() = _liveData

    val gson = GsonBuilder().setPrettyPrinting().create()
    var job: Job? = null

    private fun String.prettyPrint(): String {
        return gson.toJson(JsonParser.parseString(this))
    }

    fun getGroceryList(roomcode: String) {
        if(job?.isActive == true) {
            return
        }

        _liveData.postValue(JSONObject())

        // Launch the request in the background
        job = viewModelScope.launch {
            try {
                val rawJSON: String = getRequest("/grocery/roomcode/$roomcode")
                _liveData.postValue(parseJSON(rawJSON))

            } catch (e: Exception) {
                _liveData.postValue(JSONObject())
            }
        }
    }

    private fun parseJSON(data: String?): JSONObject {
        try {
            return JSONTokener(data).nextValue() as JSONObject
        } catch (e: Exception) {
            return JSONObject()
        }
    }

    private suspend fun getRequest(url: String): String =
        withContext(Dispatchers.IO) {
            HttpClient().get(url)
        }

    private suspend fun postRequest(url: String): String =
        withContext(Dispatchers.IO) {
            HttpClient().post(url)
        }

    private suspend fun deleteRequest(url: String): String =
        withContext(Dispatchers.IO) {
            HttpClient().delete(url)
        }

    companion object {
        private const val TAG = "RoommateApp"
    }
}
