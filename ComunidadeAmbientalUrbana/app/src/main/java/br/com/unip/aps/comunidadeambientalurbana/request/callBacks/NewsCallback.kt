package br.com.unip.aps.comunidadeambientalurbana.request.callBacks

import br.com.unip.aps.comunidadeambientalurbana.request.dtos.News

interface NewsCallback {

    fun volleyResponse(news: List<News>)
}