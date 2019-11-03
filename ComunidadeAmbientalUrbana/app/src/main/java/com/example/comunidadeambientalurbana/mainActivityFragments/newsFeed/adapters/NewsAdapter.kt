package com.example.comunidadeambientalurbana.mainActivityFragments.newsFeed.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.comunidadeambientalurbana.NewsDetailsActivity
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
//        Define os valores das views de cada card de noticia
        Picasso.get().load(newsList[position].image?.thumbnail?.contentUrl).into(holder.mImage)
        holder.mName.text = newsList[position].name
        holder.mPublisher.text = "${context.getText(R.string.publisher_name)}: ${newsList[position].provider?.get(0)?.name}"
        val date = newsList[position].datePublished.subSequence(0,10).toString()

        val publishDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            "${LocalDate.parse(date).dayOfMonth}/${LocalDate.parse(date).month.value}/${LocalDate.parse(date).year}"
        } else {
            date
        }
        holder.mPublisherDate.text = "${context.getText(R.string.publish_date)}: $publishDate"

//        Listener dos cards

        holder.mCardView.setOnClickListener{

           var args = Bundle()
            args.putString("name",newsList[position].name)
            args.putString("url",newsList[position].url)
            args.putString("image",newsList[position].image?.thumbnail?.contentUrl)
            args.putString("description",newsList[position].description)
            args.putString("provider",newsList[position].provider?.get(0)?.name)
            args.putString("datePublished", publishDate)
            args.putString("category",newsList[position].category)

            val intent = Intent(context, NewsDetailsActivity::class.java)
            intent.putExtra("newsDetails",args)
            context.startActivity(intent)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.news_card, parent,false)
        return NewsViewHolder(viewHolder)
    }
}


class NewsViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {

    var mCardView: CardView = itemView.findViewById(R.id.newsCard)
    var mImage: ImageView = itemView.findViewById(R.id.detailsNewsThumbnail)
    var mName: TextView = itemView.findViewById(R.id.newsName)
    var mPublisherDate: TextView = itemView.findViewById(R.id.newsPublishDate)
    var mPublisher: TextView = itemView.findViewById(R.id.newsPublisher)

}