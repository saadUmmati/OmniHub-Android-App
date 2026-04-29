package com.example.omnihub.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.*
import com.example.omnihub.R


class NewsAdapter(
    private var newsList: List<Article>,
    private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsImage: ImageView = itemView.findViewById(R.id.ivNewsImage)
        val headline: TextView = itemView.findViewById(R.id.tvNewsHeadline)
        val source: TextView = itemView.findViewById(R.id.tvNewsSource)
        val description: TextView = itemView.findViewById(R.id.tvNewsDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_card, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = newsList[position]
        holder.headline.text = article.title
        holder.source.text = article.source.name
        holder.description.text = article.description ?: "No description available"

        // Glide image load karega
        Glide.with(holder.itemView.context)
            .load(article.urlToImage)
            .placeholder(R.drawable.ic_news_placeholder)
            .centerCrop()
            .into(holder.newsImage)

        // Click listener taake news link khule
        holder.itemView.setOnClickListener {
            // Intent code yahan aayega
            article.url?.let { url -> onItemClick(url) }
        }
    }



    override fun getItemCount(): Int = newsList.size

    fun updateData(newList: List<Article>) {
        newsList = newList
        notifyDataSetChanged()
    }
}