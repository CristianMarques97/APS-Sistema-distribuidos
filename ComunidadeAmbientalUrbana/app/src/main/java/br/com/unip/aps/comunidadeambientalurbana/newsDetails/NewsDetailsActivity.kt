package br.com.unip.aps.comunidadeambientalurbana.newsDetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.unip.aps.comunidadeambientalurbana.R
import br.com.unip.aps.comunidadeambientalurbana.newsDetails.commentaryModule.adapters.CommentaryAdapter
import br.com.unip.aps.comunidadeambientalurbana.request.RequestService
import br.com.unip.aps.comunidadeambientalurbana.request.callBacks.CommentsCallbacks
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.Commentary
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class NewsDetailsActivity : AppCompatActivity(), CommentsCallbacks {

    var newsUrl = ""
    var commentsList = mutableListOf<Commentary>()
    lateinit var recyclerView: RecyclerView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Configura o tema da activity
        val prefs = getSharedPreferences("appConfig", Context.MODE_PRIVATE)
        setTheme(if(prefs.getBoolean("darkTheme", false)) {
            R.style.DarkThemeAppTheme
        } else {
            R.style.AppTheme
        })

//        Recupera dos dados enviados na intent e os define nas views da activity

        title = getText(R.string.details_activity_name)
        val args =intent?.extras?.getBundle("newsDetails")
        setContentView(R.layout.activity_news_details)

        findViewById<TextView>(R.id.detailsNewsTitle).text = args?.getString("name")
        Picasso.get().load(args?.getString("image")).into(findViewById<ImageView>(R.id.detailsNewsThumbnail))
        findViewById<TextView>(R.id.detailsNewsProvider).text = "${getText(R.string.publisher_name)}: ${args?.getString("provider")}"
        findViewById<TextView>(R.id.detailsNewsContentDescription).text = args?.getString("description")
        newsUrl = args?.getString("url").toString()
        findViewById<TextView>(R.id.articlePublishDate).text = args?.getString("datePublished")
        findViewById<TextView>(R.id.newsCategory).text = args?.getString("category")

//        define o botão voltar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//      Inicia o listener do firestore para buscar os comentários da notícia
        RequestService.initFirestoreListener(this, newsUrl)
    }


//    função do botão de compartilhar
        fun shareButton(view: View) {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_TEXT, intent?.extras?.getBundle("newsDetails")?.getString("url"))
                shareIntent.type = "text/plain"
                startActivity(Intent.createChooser(shareIntent, "${getText(R.string.shareNews)}: "))
        }

//    Navega para activity com a página carregada em uma webView
    fun navigateNews(view: View) {
        intent = Intent(this, WebNewsActivity::class.java)
        intent.putExtra("newsUrl", newsUrl)
        startActivity(intent)
    }



    override fun onStart() {
        super.onStart()
//        Inicializa a lista de comentários
        recyclerView = findViewById(R.id.commentsRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    fun sendCommentary(view: View) {
//        Verifica se o campo de comentários está vazio
        if(findViewById<EditText>(R.id.editComments).text.toString().isEmpty()) {
//            Apresenta a snackbar e não envia o comentário ao firebase
            val snack = Snackbar.make(findViewById(R.id.floatingActionButton),getString(R.string.empty_comment), Snackbar.LENGTH_SHORT)
            val view = snack.view
            val tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            tv.setTextColor(getColor(R.color.snackbar_button))
            snack.show()
            return
        }
//        envia o comentário ao firebase
        RequestService.addCommentary(this, findViewById<EditText>(R.id.editComments).text.toString(), newsUrl, commentsList)
    }

//    Callback do retorno do firebase
    override fun onCommentaryAdd() {
        findViewById<EditText>(R.id.editComments).text = null
        val snack = Snackbar.make(findViewById(R.id.floatingActionButton),getString(R.string.commentary_send), Snackbar.LENGTH_SHORT)
        val view = snack.view
                val tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                        tv.setTextColor(getColor(R.color.snackbar_button))
            snack.show()
    }

//    Callback do retorno do firebase
    @Suppress("UNCHECKED_CAST")
    override fun onCommentaryListChanged(comment: MutableMap<String, Any>) {
//    Se a lista de comentários estiver vazia, mostrar o texto de que a notícia não possui nenhum comentário
        if(comment.isEmpty()) {
           recyclerView.visibility = View.GONE
            findViewById<TextView>(R.id.noCommentaryText).visibility = View.VISIBLE
            return
        }
//    extrai a lista de comentários
        val listOfComments = comment["comentarios"] as ArrayList<String>
//    Re-inicia a lista de comentários
        commentsList = mutableListOf()
//    Separa o nome e o comentário e cria um novo objetc de comentário
        listOfComments.forEach {
            Log.d("nome", it)
            var nome = ""
            var i = 0
            while (it[i] != '-') {
                nome += it[i]
                i++
            }
            nome = nome.substring(0, nome.length - 1)
            val formatedComment = it.substring(nome.length + 3)
//          adiciona o comentário a lista
            commentsList.add(Commentary(nome, formatedComment))

        }
//          adiciona a lista de comentários ao adapter
        recyclerView.adapter = CommentaryAdapter(this, commentsList)

//          Apresenta a lista de comentários
        findViewById<TextView>(R.id.noCommentaryText).visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }


}

