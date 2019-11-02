package com.example.comunidadeambientalurbana.request

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import com.example.comunidadeambientalurbana.MainActivity
import com.example.comunidadeambientalurbana.environment.Environment
import com.example.comunidadeambientalurbana.request.dtos.FeedNews
import org.json.JSONObject

class RequestService {

    companion object {      // todos os metodos e atributos dentro deste bloco é o mesmo que se fosse declarados como "static" em JAVA

            fun getNewsByTopic(activity: Activity, topic: String) {
                val fila = Volley.newRequestQueue(activity)   // Cria um objeto para adicionar a fila de requisições
                val url = "${Environment.FEED_API_BASE_URL}?${Environment.REGION}&q=$topic" // URL da requisição
                Log.d("Resquest Url", url)
                    // Cria um objeto contendo a requisição
                val request = object: StringRequest(
                    Request.Method.GET,     // metodo de requisição HTTP
                    url,
                    Response.Listener { response ->   // Listener de resposta com sucesso
                        MainActivity.newsFeed = Klaxon().parse<FeedNews>(response.toString())!!
                        Log.i("Bing News Request", response)

                    }, Response.ErrorListener { error ->    // Listener de resposta com Falha
                        Toast.makeText(activity, "Houver um erro ao carregar as notícias", Toast.LENGTH_SHORT).show()
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