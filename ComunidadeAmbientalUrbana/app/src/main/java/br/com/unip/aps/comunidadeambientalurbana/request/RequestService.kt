package br.com.unip.aps.comunidadeambientalurbana.request

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import br.com.unip.aps.comunidadeambientalurbana.R
import br.com.unip.aps.comunidadeambientalurbana.environment.Environment
import br.com.unip.aps.comunidadeambientalurbana.request.callBacks.CommentsCallbacks
import br.com.unip.aps.comunidadeambientalurbana.request.callBacks.NewsCallback
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.Commentary
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.FeedNews
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import com.google.firebase.firestore.FirebaseFirestore

class RequestService {

    companion object {      // todos os metodos e atributos dentro deste bloco é o mesmo que se fosse declarados como "static" em JAVA

            fun getNewsByTopic(activity: Activity, topic: String) {
                val fila = Volley.newRequestQueue(activity)   // Cria um objeto para adicionar a fila de requisições
                val url = "${Environment.FEED_API_BASE_URL}?${Environment.REGION}&q=$topic&count=${Environment.QTDE_NEWS}" // URL da requisição
                Log.d("Resquest Url", url)
                    // Cria um objeto contendo a requisição
                val request = object: StringRequest(
                    Method.GET,     // metodo de requisição HTTP
                    url,
                    Response.Listener { response ->   // Listener de resposta com sucesso
                        try {
//                            Converte JSON do servidor da azure para Objeto
                            val newsFeed = Klaxon().parse<FeedNews>(response.toString())!!
//                          Chama o callback da requisição
                            if(activity is NewsCallback) {
                               val listener = activity as NewsCallback
                               listener.onNewsReceived(newsFeed.value.toList())
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

        fun addCommentary(context: Activity, comment: String, newsUrl: String, commentList: MutableList<Commentary>) {
//           Busca a instancia do firebase
            val firebaseDatabase = FirebaseFirestore.getInstance()
//            Adiciona o nome do usuário ao comentário
            val newComment = "${context.getSharedPreferences("appConfig", Context.MODE_PRIVATE).getString("user-name", "")} - $comment"
//            Adiciona o comentário em uma lista dinâmica
            val users = mutableListOf(newComment)
//             Realiza um merge da lista de comentário com a lista que possui o novo usuário, realizando a formatação com os nomes do usuário
            commentList.forEach {
                users.add("${it.nome} - ${it.commentary}")
            }
//            Cria a HashMap com o modelo do firebase
            val user = hashMapOf("comentarios" to users)
//          Remove protocolo e barras e utiliza a url como chave no firebase
            val urlID = newsUrl.replace("/", "").replace("https:","").replace("http:","")
//          Adiciona a nova lista de comentários
            firebaseDatabase.collection(Environment.firestorePath[0])
                .document(urlID)
                .set(user)
                .addOnSuccessListener {response ->
                    Log.d("Sucesso","Envio com Sucesso")
//                  Realiza o callback da requisição
                    if (context is CommentsCallbacks) {
                        val listener = context as CommentsCallbacks
                        listener.onCommentaryAdd()
                    }
                }
        }

//        Função que inicia o listener do firestore
        fun initFirestoreListener(context: Activity, newsUrl: String) {
//    Busca a instancia do firebase
            val firebaseDatabase = FirebaseFirestore.getInstance()
//    Caminho do firebase
            firebaseDatabase.collection(Environment.firestorePath[0])
                .document(newsUrl.replace("/", "").replace("https:","").replace("http:",""))
//                    Adiciona um listener para escutar mudança na base de dados(Firestore)
                .addSnapshotListener {snapshot, e ->
//                    Verificação de sucesso no retorno
                    if(e!=null) {
                        Log.e("Firebase Error", "Erro ao se conectar ao firebase Snapshot")
                    }
//                      Verifica se hove mudanças no firestore
                    if(snapshot != null && snapshot.exists()) {
                        Log.d("Snapshot return", "${snapshot.data}")
//                      Chama o callback da requisição
                        if (context is CommentsCallbacks) {
                            val listener = context as CommentsCallbacks
                            snapshot.data?.let { listener.onCommentaryListChanged(it) }
                        }
                    }

                }


        }
    }


}