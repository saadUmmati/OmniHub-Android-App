package com.example.omnihub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omnihub.classes.Article
import com.example.omnihub.classes.NewsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class newsFragment : Fragment(R.layout.fragment_news) {

    private lateinit var newsAdapter: NewsAdapter
    private val newsList = ArrayList<Article>() // Data store karne ke liye list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. UI ko initialize karna
        val rvNews = view.findViewById<RecyclerView>(R.id.rvNews)
        newsAdapter = NewsAdapter(newsList) { url ->
            // Browser mein URL open karne ka tareeqa
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), android.net.Uri.parse(url))
        }
        rvNews.layoutManager = LinearLayoutManager(requireContext())
        rvNews.adapter = newsAdapter

        // 2. Data fetch karna
        loadNewsFromServer()
    }

    private fun loadNewsFromServer() {
        // lifecycleScope background thread chalata hai taake screen freeze na ho
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.newsApi.getTopHeadlines()
                if (response.isSuccessful && response.body() != null) {
                    val articles = response.body()!!.articles

                    // UI hamesha Main Thread par update hoti hai
                    withContext(Dispatchers.Main) {
                        newsList.clear()
                        newsList.addAll(articles)
                        newsAdapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Internet error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}