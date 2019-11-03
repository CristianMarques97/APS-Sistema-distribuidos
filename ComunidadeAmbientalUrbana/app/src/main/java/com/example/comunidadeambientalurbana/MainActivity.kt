package com.example.comunidadeambientalurbana

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.comunidadeambientalurbana.request.RequestService
import com.example.comunidadeambientalurbana.request.dtos.FeedNews

class MainActivity : AppCompatActivity() {
   companion object { lateinit var newsFeed: FeedNews}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("appConfig", Context.MODE_PRIVATE)
        setTheme(if(prefs.getBoolean("darkTheme", false)) {R.style.DarkThemeAppTheme} else {R.style.AppTheme})

        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_my_feed, R.id.navigation_preferences))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        //        RequestService.getNewsByTopic(this, "Ambiente")


    }


}
