package com.example.omnihub.classes

import com.example.omnihub.NewsApiService
import com.example.omnihub.RetrofitClient

class NewsRepository(private val apiService: NewsApiService) {
    suspend fun fetchNews() = apiService.getTopHeadlines()
    suspend fun getArticles(): List<Article>? {
        // Yahan newsApi use hoga
        val response = RetrofitClient.newsApi.getTopHeadlines(
            country = "us",
            pageSize = 50,
            apiKey = "4519496843e84f9b9555d77218a56e92"
        )

        return if (response.isSuccessful) {
            response.body()?.articles
        } else {
            null
        }
    }
}