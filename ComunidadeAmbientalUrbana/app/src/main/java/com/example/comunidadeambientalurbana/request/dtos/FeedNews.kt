package com.example.comunidadeambientalurbana.request.dtos

import com.beust.klaxon.Json
import java.io.Serializable

// Conjunto de data class para converter o json da requisição em objeto
// data classes apenas armazenam informações, não possuem funções ou operações

data class FeedNews(
    @Json(ignored = true) var _type: String   = "",
    @Json(ignored = true) var readLink: String   = "",
    @Json(ignored = true) var queryContext: Any? = null,
    var totalEstimatedMatches: Number = 0,
    @Json(ignored = true) var sort: Any? = null,
    var value: Array<News>
    ): Serializable

data class News(
    var name: String  = "",
    var url:  String  = "",
    var image: NewsImages?  = null,
    var description: String  = "",
    var provider: Array<NewsProvider>? = null ,
    var datePublished: String = "",
    var category: String? = ""
): Serializable

data class NewsImages(var thumbnail: Thumbnail? = null): Serializable

data class Thumbnail(
    var contentUrl: String = "",
    var width: Number = 0,
    var height: Number = 0
)

data class NewsProvider(
    var _type: String = "",
    var name: String = ""
): Serializable
