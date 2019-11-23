package br.com.unip.aps.comunidadeambientalurbana.newsDetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import br.com.unip.aps.comunidadeambientalurbana.R
import java.util.zip.Inflater

class WebNewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("appConfig", Context.MODE_PRIVATE)
        setTheme(if(prefs.getBoolean("darkTheme", false)) {
            R.style.DarkAppThemeNoActionBarWebActivity
        } else {
            R.style.AppThemeNoActionBar
        })

        setContentView(R.layout.activity_web_news)
        setSupportActionBar(findViewById(R.id.webViewToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mWebView = findViewById<WebView>(R.id.newsWeb)
        mWebView.webViewClient = NewsWebViewCustomClient()
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.displayZoomControls = true
        mWebView.loadUrl(intent?.extras?.getString("newsUrl"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.web_view_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId) {
            R.id.openInChrome -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intent?.extras?.getString("newsUrl")))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setPackage("com.android.chrome")
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}

class NewsWebViewCustomClient() : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
        view?.loadUrl(url)
        return true
    }


}
