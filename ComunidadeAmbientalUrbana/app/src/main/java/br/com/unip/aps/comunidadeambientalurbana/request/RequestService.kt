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
import br.com.unip.aps.comunidadeambientalurbana.request.callBacks.CommentsCallbacks
import br.com.unip.aps.comunidadeambientalurbana.request.callBacks.NewsCallback
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.Commentary
import com.google.firebase.firestore.FirebaseFirestore
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

                            if(activity is NewsCallback) {
                               val listener = activity as NewsCallback
                               listener.volleyResponse(newsFeed.value.toList())
                            }

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


        fun getComments(newsID: String, context: Activity): MutableList<Commentary> {
            var commentList = mutableListOf<Commentary>()

            val firebaseDatabase = FirebaseFirestore.getInstance()

            val collection = newsID.replace("/", "").replace("https:","").replace("http:","")

            firebaseDatabase.collection(Environment.firestorePath[0])
                .document(collection)
                .get()
                .addOnSuccessListener { result ->

                    try {
                        val data = result.data?.get("comentários") as ArrayList<String>
                        for (comments in data) {
                            var nome = ""
                            var i = 0
                            while (comments[i] !== '-') {
                                nome += comments[i]
                                i++
                            }
                            nome = nome.substring(0, nome.length - 1)
                            var comment = comments.substring(nome.length + 2)

                            commentList.add(Commentary(nome, comment))

                        }
                        Log.d("result", "${data}")

                        if (context is CommentsCallbacks) {
                            val listener = context as CommentsCallbacks
                            listener.volleyResponse(commentList)
                        }

                    }catch (e: Exception) {

                    }
                }

            return commentList

        }
    }


}