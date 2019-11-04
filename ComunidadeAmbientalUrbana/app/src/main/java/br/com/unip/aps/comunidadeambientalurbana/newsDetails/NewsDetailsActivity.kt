package br.com.unip.aps.comunidadeambientalurbana.newsDetails

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import br.com.unip.aps.comunidadeambientalurbana.R
import br.com.unip.aps.comunidadeambientalurbana.environment.Environment
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.Commentary
import com.beust.klaxon.Klaxon
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class NewsDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("appConfig", Context.MODE_PRIVATE)
        setTheme(if(prefs.getBoolean("darkTheme", false)) {
            R.style.DarkAppThemeNoActionBar
        } else {
            R.style.AppThemeNoActionBar
        })

        title = getText(R.string.details_activity_name)
        val args =intent?.extras?.getBundle("newsDetails")
        setContentView(R.layout.activity_news_details)

        findViewById<TextView>(R.id.detailsNewsTitle).text = args?.getString("name")
        Picasso.get().load(args?.getString("image")).into(findViewById<ImageView>(R.id.detailsNewsThumbnail))
        findViewById<TextView>(R.id.detailsNewsProvider).text = "${getText(
            R.string.publisher_name
        )}: ${args?.getString("provider")}"
        findViewById<TextView>(R.id.detailsNewsContentDescription).text = args?.getString("description")
        findViewById<TextView>(R.id.newsUrl).setOnClickListener{ Toast.makeText(this, "Navegando Para: ${args?.getString("url")}", Toast.LENGTH_SHORT).show()}
        findViewById<TextView>(R.id.articlePublishDate).text = args?.getString("datePublished")
        findViewById<TextView>(R.id.newsCategory).text = args?.getString("category")

        val toolbar = findViewById<Toolbar>(R.id.detailsToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.news_details_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.shareNews -> {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_TEXT, intent?.extras?.getBundle("newsDetails")?.getString("url"))
                shareIntent.type = "text/plain"
                startActivity(Intent.createChooser(shareIntent, "${getText(R.string.shareNews)}: "))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        val args =intent?.extras?.getBundle("newsDetails")
       val noticiaUrl = args!!.getString("url")

        val firebaseDatabase = FirebaseFirestore.getInstance()

        val collection = noticiaUrl.replace("/", "").replace("https:","").replace("http:","")

        firebaseDatabase.collection(Environment.firestorePath[0])
            .document(collection)
            .get()
            .addOnSuccessListener { result ->

               val data = result.data?.get("comentários") as ArrayList<String>
                val commentList = mutableListOf<Commentary>()
                for(comments in data) {
                    var nome = ""
                    var comment = ""
                    var i = 0
                    while(comments[i] !== '-') {
                        nome += comments[i]
                        i ++
                    }
                    nome = nome.substring(0, nome.length-1)
                    comment = comments.substring(nome.length + 2)

                    commentList.add(Commentary(nome,comment))

                }
                Log.d("result", "${data}")

            }


    }


}

