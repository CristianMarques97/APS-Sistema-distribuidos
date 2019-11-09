package br.com.unip.aps.comunidadeambientalurbana.newsDetails

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val prefs = getSharedPreferences("appConfig", Context.MODE_PRIVATE) //
        setTheme(if(prefs.getBoolean("darkTheme", false)) {          // Configura o modo escuro
            R.style.DarkThemeAppTheme                                             //
        } else {                                                                  //
            R.style.AppTheme
        })


//        Recupera dos dados enviados na intent

        title = getText(R.string.details_activity_name)
        val args =intent?.extras?.getBundle("newsDetails")
        setContentView(R.layout.activity_news_details)

        findViewById<TextView>(R.id.detailsNewsTitle).text = args?.getString("name")
        Picasso.get().load(args?.getString("image")).into(findViewById<ImageView>(R.id.detailsNewsThumbnail))
        findViewById<TextView>(R.id.detailsNewsProvider).text = "${getText(
            R.string.publisher_name
        )}: ${args?.getString("provider")}"
        findViewById<TextView>(R.id.detailsNewsContentDescription).text = args?.getString("description")
        newsUrl = args?.getString("url").toString()
        findViewById<TextView>(R.id.articlePublishDate).text = args?.getString("datePublished")
        findViewById<TextView>(R.id.newsCategory).text = args?.getString("category")

//        define o botão voltar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        RequestService.initFirestoreListener(this, newsUrl)
    }


//    função do botão de compartilhar
        fun shareButton(view: View){
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
        recyclerView = findViewById(R.id.commentsRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    fun sendCommentary(view: View) {
        if(findViewById<EditText>(R.id.editComments).text.toString().isEmpty()) {
            val snack = Snackbar.make(findViewById(R.id.floatingActionButton2),getString(R.string.empty_comment), Snackbar.LENGTH_SHORT)
            val view = snack.view
            val tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            tv.setTextColor(getColor(R.color.snackbar_button))
            snack.show()
            return
        }
        RequestService.addCommentary(this, findViewById<EditText>(R.id.editComments).text.toString(), newsUrl, commentsList)
    }


    override fun onCommentaryReceived(commentsList: MutableList<Commentary>) {
        this.commentsList = commentsList
        Log.d("Lista",commentsList.toString())

    }

    override fun onCommentaryAdd() {
        findViewById<EditText>(R.id.editComments).text = null
        val snack = Snackbar.make(findViewById(R.id.floatingActionButton2),getString(R.string.commentary_send), Snackbar.LENGTH_SHORT)
        val view = snack.view
                val tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                        tv.setTextColor(getColor(R.color.snackbar_button))
            snack.show()
    }

    override fun onCommentaryListChanged(comment: MutableMap<String, Any>) {
        if(comment.isEmpty()) {
           recyclerView.visibility = View.GONE
            findViewById<TextView>(R.id.noCommentaryText).visibility = View.VISIBLE
            return
        }
        val listOfComments = comment["comentarios"] as ArrayList<String>
        commentsList = mutableListOf()
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

            commentsList.add(Commentary(nome, formatedComment))

        }

        recyclerView.adapter = CommentaryAdapter(this, commentsList)

        findViewById<TextView>(R.id.noCommentaryText).visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }


}

