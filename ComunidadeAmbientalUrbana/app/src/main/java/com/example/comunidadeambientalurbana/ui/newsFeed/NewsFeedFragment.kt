package com.example.comunidadeambientalurbana.ui.newsFeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.comunidadeambientalurbana.R
import com.example.comunidadeambientalurbana.request.RequestService

class NewsFeedFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.findViewById<ConstraintLayout>(R.id.LoadLayout)?.visibility = View.VISIBLE
         RequestService.getNewsByTopic(activity!!, "ambiente")

        return inflater.inflate(R.layout.news_feed, container, false)
    }

    override fun onStart() {
        super.onStart()


    }
}