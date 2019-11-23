package br.com.unip.aps.comunidadeambientalurbana.environment

class Environment {

    companion object {
        const val FEED_API_BASE_URL = "https://aps-news-feed.cognitiveservices.azure.com/bing/v7.0/news/search"
        const val FEED_API_KEY = "69a82928898842128b420d2c53958c16"
        const val REGION = "mkt=pt-BR"
        const val QTDE_NEWS = "100"
        val firestorePath = arrayOf("noticia","comentarios")
        const val newsTopic = "meio-ambiente"
    }
}