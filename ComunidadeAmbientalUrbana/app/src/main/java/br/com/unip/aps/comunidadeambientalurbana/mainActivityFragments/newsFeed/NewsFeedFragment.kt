package br.com.unip.aps.comunidadeambientalurbana.mainActivityFragments.newsFeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import br.com.unip.aps.comunidadeambientalurbana.FeedPlug
import br.com.unip.aps.comunidadeambientalurbana.R
import br.com.unip.aps.comunidadeambientalurbana.mainActivityFragments.newsFeed.adapters.NewsAdapter
import br.com.unip.aps.comunidadeambientalurbana.request.RequestService
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.News

class NewsFeedFragment : Fragment() {
lateinit var newsFeed: List<News>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        Caso a lista já esteja carregada ela sera buscada da activity

        val listener: FeedPlug
//        Verifica se a activity do fragment implementa a interface de comunicação
        if(activity!!.parent is FeedPlug){
            listener = activity!!.parent as FeedPlug
//            Busca a lista de notícias
            newsFeed = listener.getNews()
//            define o adapter e alterna a visibilidade
            activity?.findViewById<RecyclerView>(R.id.newsList)?.adapter = NewsAdapter(activity!!, newsFeed)
            activity?.findViewById<RecyclerView>(R.id.newsList)?.visibility = View.VISIBLE
            activity?.findViewById<ProgressBar>(R.id.newsLoadProgress)?.visibility = View.GONE
        } else {
//            Se a lista estiver vazia buscar as notícias na Azure
            activity?.findViewById<ProgressBar>(R.id.newsLoadProgress)?.visibility =
                View.VISIBLE
            RequestService.getNewsByTopic(activity!!, "ambiente")
        }
        return inflater.inflate(R.layout.news_feed, container, false)
    }

}