package com.example.omnihub

import com.example.omnihub.classes.NewsResponse
import okhttp3.Response
import retrofit2.http.*

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("pageSize") pageSize: Int = 50, // 50 news mangwa li
        @Query("apiKey") apiKey: String = "4519496843e84f9b9555d77218a56e92",
        @Header("User-Agent") userAgent: String = "OmniHubApp" // Ye lazmi hai
    ): retrofit2.Response<NewsResponse>
}