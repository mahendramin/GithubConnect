package com.example.githubconnect.ui.detail.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubconnect.data.remote.response.ItemsItem
import com.example.githubconnect.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    private var _followersList = MutableLiveData<List<ItemsItem>>()
    val followersList: LiveData<List<ItemsItem>> = _followersList

    private var _followingList = MutableLiveData<List<ItemsItem>>()
    val followingList: LiveData<List<ItemsItem>> = _followingList

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowers(username: String) {
        val client = ApiConfig.getApiService().getUserFollower(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followersList.value = response.body()
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getFollowing(username: String) {
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followingList.value = response.body()
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private companion object {
        const val TAG = "FollowViewModel"
    }
}