package com.example.githubconnect.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubconnect.data.remote.response.ItemsItem
import com.example.githubconnect.data.remote.response.SearchResponse
import com.example.githubconnect.data.retrofit.ApiConfig
import com.example.githubconnect.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private var _searchResult = MutableLiveData<List<ItemsItem>>()
    val searchResult: LiveData<List<ItemsItem>> = _searchResult

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isSearchResultEmpty = MutableLiveData<Event<Boolean>>()
    val isSearchResultEmpty: LiveData<Event<Boolean>> = _isSearchResultEmpty

    fun searchUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.totalCount?.let {
                        if (it > 0) {
                            _searchResult.value = response.body()?.items
                        } else _isSearchResultEmpty.value = Event(true)
                    }
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private companion object {
        const val TAG = "HomeViewModel"
    }
}
