package com.example.comunidadeambientalurbana.ui.newsFeed.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.comunidadeambientalurbana.R
import com.example.comunidadeambientalurbana.request.dtos.News
import com.squareup.picasso.Picasso
import java.time.LocalDate

class NewsAdapter constructor(): RecyclerView.Adapter<NewsViewHolder>() {

    lateinit var context: Context
    lateinit var newsList: List<News>

    constructor(context: Context, newsList: List<News>): this(){
        this.context = context
        this.newsList = newsList

    }
    override fun getItemCount(): Int {
            return  newsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        Picasso.get().load(newsList[position].image?.thumbnail?.contentUrl).into(holder.mImage)
        holder.mName.text = newsList[position].name
        holder.mPublisher.text = "${context.getText(R.string.publisher_name)}: ${newsList[position].provider?.get(0)?.name}"
        val date = newsList[position].datePublished.subSequence(0,10).toString()
        var publishDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            "${LocalDate.parse(date).dayOfMonth}/${LocalDate.parse(date).month.value}/${LocalDate.parse(date).year}"
        } else {
            date
        }

        holder.mPublisherDate.text = "${context.getText(R.string.publish_date)}: $publishDate"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.news_card, parent,false)
        return NewsViewHolder(viewHolder)
    }
}


class NewsViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {

     var mImage: ImageView
     var mName: TextView
     var mPublisherDate: TextView
     var mPublisher: TextView

  init {
      this.mImage = itemView.findViewById(R.id.newsThumbnail)
      this.mName = itemView.findViewById(R.id.newsName)
      this.mPublisher = itemView.findViewById(R.id.newsPublisher)
      this.mPublisherDate = itemView.findViewById(R.id.newsPublishDate)

  }
}