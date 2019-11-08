package br.com.unip.aps.comunidadeambientalurbana.newsDetails

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import br.com.unip.aps.comunidadeambientalurbana.R
import br.com.unip.aps.comunidadeambientalurbana.environment.Environment
import br.com.unip.aps.comunidadeambientalurbana.request.RequestService
import br.com.unip.aps.comunidadeambientalurbana.request.callBacks.CommentsCallbacks
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.Commentary
import com.beust.klaxon.Klaxon
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class NewsDetailsActivity : AppCompatActivity(), CommentsCallbacks {

    var newsUrl = ""
    lateinit var commentsList: MutableList<Commentary>
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

        RequestService.getComments(newsUrl, this)
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
    }

    fun sendCommentary(view: View) {
        var snack = Snackbar.make(findViewById(R.id.floatingActionButton2),"Comentário Enviado", Snackbar.LENGTH_SHORT)
        var view = snack.view
                var tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                        tv.setTextColor(getColor(R.color.colorAccent))
            snack.show()
    }


    override fun volleyResponse(commentsList: MutableList<Commentary>) {

        this.commentsList = commentsList
        Log.d("Lista",commentsList.toString())

    }


}

