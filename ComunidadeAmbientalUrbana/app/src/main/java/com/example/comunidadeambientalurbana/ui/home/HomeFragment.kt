package com.example.comunidadeambientalurbana.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.comunidadeambientalurbana.MainActivity
import com.example.comunidadeambientalurbana.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        textView.setOnClickListener {
            var ex = MainActivity.newsFeed
            Log.i("debug", MainActivity.newsFeed.toString())
        }
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}