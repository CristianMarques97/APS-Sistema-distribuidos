package br.com.unip.aps.comunidadeambientalurbana

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.unip.aps.comunidadeambientalurbana.mainActivityFragments.newsFeed.adapters.NewsAdapter
import br.com.unip.aps.comunidadeambientalurbana.request.callBacks.NewsCallback
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.FeedNews
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.News
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity(), NewsCallback {


    companion object { lateinit var newsFeed: FeedNews }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        val prefs = getSharedPreferences("appConfig", Context.MODE_PRIVATE)
        setTheme(if(prefs.getBoolean("darkTheme", false)) {
            R.style.DarkThemeAppTheme
        } else {
            R.style.AppTheme
        })

        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_my_feed,
            R.id.navigation_preferences
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        //        RequestService.getNewsByTopic(this, "Ambiente")


    }

//    Callback da Resposta do bing
    override fun volleyResponse(news: List<News>) {
        val recycleView = this.findViewById<RecyclerView>(R.id.newsList)
        recycleView.layoutManager = GridLayoutManager(this, 1)
        val newsAdapter =
            NewsAdapter(
                this,
                news
            )
        recycleView.adapter = newsAdapter
        this.findViewById<ConstraintLayout>(R.id.LoadLayout).visibility =
            View.GONE
    }

}
