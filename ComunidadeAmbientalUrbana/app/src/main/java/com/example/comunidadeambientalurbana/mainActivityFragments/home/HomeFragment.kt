package com.example.comunidadeambientalurbana.mainActivityFragments.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onStart() {
        super.onStart()
        val prefs = activity?.getSharedPreferences("appConfig", Context.MODE_PRIVATE)

        activity?.findViewById<TextView>(R.id.welcome)?.text =
            "${getText(R.string.welcome)} ${if (prefs?.getString(
                    "user-name",
                    getText(R.string.no_user_name) as String?
                ) != ""
            ) {
                prefs?.getString(
                    "user-name",
                    getText(R.string.no_user_name) as String?
                )
            } else {
                getText(R.string.no_user_name)
            }}"

    }
}