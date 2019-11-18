package br.com.unip.aps.comunidadeambientalurbana.mainActivityFragments.newsFeed

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.unip.aps.comunidadeambientalurbana.FeedPlug
import br.com.unip.aps.comunidadeambientalurbana.R
import br.com.unip.aps.comunidadeambientalurbana.environment.Environment
import br.com.unip.aps.comunidadeambientalurbana.mainActivityFragments.newsFeed.adapters.NewsAdapter
import br.com.unip.aps.comunidadeambientalurbana.request.RequestService
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsFeedFragment : Fragment() {

    companion object {
        var firstLoad = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.findViewById<RecyclerView>(R.id.newsList)?.visibility = View.GONE
        activity?.findViewById<ProgressBar>(R.id.newsLoadProgress)?.visibility = View.VISIBLE

        return inflater.inflate(R.layout.news_feed, container, false)
    }

    override fun onStart() {
        super.onStart()
            activity?.findViewById<SwipeRefreshLayout>(R.id.newsListRefresher)?.setOnRefreshListener{ RequestService.getNewsByTopic(activity!!, Environment.newsTopic)}
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)
            ?.menu?.findItem(R.id.navigation_my_feed)?.isEnabled = false
//        Re-aproveita a lista já buscada na Azure
        if (firstLoad) {
            RequestService.getNewsByTopic(activity!!, Environment.newsTopic)
        } else {
            if (activity!! is FeedPlug) {
                val listener = activity!! as FeedPlug
                val recyclerView = activity?.findViewById<RecyclerView>(R.id.newsList)
                recyclerView?.layoutManager =
                    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        GridLayoutManager(activity!!, 1)
                    } else {
                        GridLayoutManager(activity!!, 2)
                    }
                recyclerView?.adapter = NewsAdapter(activity!!, listener.getNews())
                recyclerView?.visibility = View.VISIBLE
                activity?.findViewById<ProgressBar>(R.id.newsLoadProgress)?.visibility = View.GONE

            }
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)
            ?.menu?.findItem(R.id.navigation_my_feed)?.isEnabled = true
    }
}