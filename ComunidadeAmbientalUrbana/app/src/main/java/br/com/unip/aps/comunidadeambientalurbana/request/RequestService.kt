package br.com.unip.aps.comunidadeambientalurbana.request

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.unip.aps.comunidadeambientalurbana.R
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import br.com.unip.aps.comunidadeambientalurbana.environment.Environment
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.FeedNews
import br.com.unip.aps.comunidadeambientalurbana.mainActivityFragments.newsFeed.adapters.NewsAdapter
import java.lang.Exception

class RequestService {

    companion object {      // todos os metodos e atributos dentro deste bloco é o mesmo que se fosse declarados como "static" em JAVA

            fun getNewsByTopic(activity: Activity, topic: String) {
                val fila = Volley.newRequestQueue(activity)   // Cria um objeto para adicionar a fila de requisições
                val url = "${Environment.FEED_API_BASE_URL}?${Environment.REGION}&q=$topic&count=${Environment.QTDE_NEWS}" // URL da requisição
                Log.d("Resquest Url", url)
                    // Cria um objeto contendo a requisição
                val request = object: StringRequest(
                    Request.Method.GET,     // metodo de requisição HTTP
                    url,
                    Response.Listener { response ->   // Listener de resposta com sucesso
                        try {
                            val newsFeed = Klaxon().parse<FeedNews>(response.toString())!!


                            val recycleView = activity.findViewById<RecyclerView>(R.id.newsList)
                            recycleView.layoutManager = GridLayoutManager(activity, 1)
                            val newsAdapter =
                                NewsAdapter(
                                    activity,
                                    newsFeed.value.toList()
                                )
                            recycleView.adapter = newsAdapter
                            activity.findViewById<ConstraintLayout>(R.id.LoadLayout).visibility =
                                View.GONE
                        }catch (ex: Exception) {

                        }
                    }, Response.ErrorListener { error ->    // Listener de resposta com Falha
                        Toast.makeText(activity, activity.getText(R.string.load_error_message), Toast.LENGTH_SHORT).show()
                    }
                )

                    // Adiciona a chave de autenticação do Bing ao cabeçalho da requisição
                {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Ocp-Apim-Subscription-Key"] = Environment.FEED_API_KEY
                        return headers
                    }
                }

                    // Envia a requisição
                fila.add(request)
            }


    }
}