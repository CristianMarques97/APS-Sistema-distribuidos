package br.com.unip.aps.comunidadeambientalurbana.environment

class Environment {

    companion object {
        const val FEED_API_BASE_URL = "https://aps-news-feed.cognitiveservices.azure.com/bing/v7.0/news/search"
        const val FEED_API_KEY = "71739fa56150402a94793f990dc9e6ac"
        const val REGION = "mkt=pt-BR"
        const val QTDE_NEWS = "100"
        val firestorePath = arrayOf("noticia","comentarios")
        const val newsTopic = "ambiente"
    }
}

//search?q=sailing+dinghies&