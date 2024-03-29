package br.com.unip.aps.comunidadeambientalurbana

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.unip.aps.comunidadeambientalurbana.mainActivityFragments.newsFeed.NewsFeedFragment
import br.com.unip.aps.comunidadeambientalurbana.mainActivityFragments.newsFeed.adapters.NewsAdapter
import br.com.unip.aps.comunidadeambientalurbana.request.callBacks.NewsCallback
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.News
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp


class MainActivity : AppCompatActivity(), NewsCallback, FeedPlug {


    lateinit var recyclerView: RecyclerView
    private lateinit var newsList: List<News>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Conecta ao firebase
        FirebaseApp.initializeApp(this)
//          Define o tema
        val prefs = getSharedPreferences("appConfig", Context.MODE_PRIVATE)
        setTheme(
            if (prefs.getBoolean("darkTheme", false)) {
                R.style.DarkThemeAppTheme
            } else {
                R.style.AppTheme
            }
        )

        setContentView(R.layout.activity_main)

//      Define a barra de navegação inferior
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_my_feed,
                R.id.navigation_preferences
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        supportActionBar?.title
    }

    //    Callback da Resposta do bing
    override fun onNewsReceived(news: List<News>) {
//    busca o recyclerView
        this.recyclerView = this.findViewById(R.id.newsList)

//    Define o layout
        this.recyclerView.layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                GridLayoutManager(this, 1)
            } else {
                GridLayoutManager(this, 2)
            }

        newsList = news

//    Define o adapter
        val newsAdapter = NewsAdapter(this, newsList)
        recyclerView.adapter = newsAdapter
//    Alterna a visibilidade entre a tela de carregamento e a lista de noticias
        this.findViewById<ProgressBar>(R.id.newsLoadProgress).visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        NewsFeedFragment.firstLoad = false
        findViewById<SwipeRefreshLayout>(R.id.newsListRefresher)?.isRefreshing = false
    }


    override fun getNews(): List<News> {
        return this.newsList
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        val recyclerView = findViewById<RecyclerView>(R.id.newsList)
        val orientation = newConfig?.orientation

        if (recyclerView != null) {
            when (orientation) {

                Configuration.ORIENTATION_LANDSCAPE -> {
                    recyclerView.layoutManager = GridLayoutManager(this, 2)
                }

                Configuration.ORIENTATION_PORTRAIT -> {
                    recyclerView.layoutManager = GridLayoutManager(this, 1)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        NewsFeedFragment.firstLoad = true
    }
}





interface FeedPlug {
    fun getNews(): List<News>
}
