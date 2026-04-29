package com.example.omnihub.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import com.example.omnihub.RetrofitClient


class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    val newsLiveData = MutableLiveData<List<Article>>()
    val errorLiveData = MutableLiveData<String>() // Errors handle karne ke liye

    fun loadNews() {
        viewModelScope.launch {
            try {
                // Hum direct Retrofit call nahi karenge, repository use karenge
                val articles = repository.getArticles()
                if (articles != null) {
                    newsLiveData.postValue(articles)
                } else {
                    errorLiveData.postValue("No news found")
                }
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }

    // Inside your ViewModel
    fun fetchNews() {
        viewModelScope.launch {
            try {
                // News API usually needs a country or category
                val response = RetrofitClient.newsApi.getTopHeadlines("us", 50, "4519496843e84f9b9555d77218a56e92")

                if (response.isSuccessful) {
                    val newsData = response.body()
                    android.util.Log.d("NewsAPI", "Success: ${newsData?.articles?.size} articles found")
                } else {
                    android.util.Log.e("NewsAPI", "Error Code: ${response.code()}")
                }
            } catch (e: Exception) {
                android.util.Log.e("NewsAPI", "Network Failure: ${e.message}")
            }
        }
    }

}