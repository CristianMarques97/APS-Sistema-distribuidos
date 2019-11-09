package br.com.unip.aps.comunidadeambientalurbana.request.callBacks

import br.com.unip.aps.comunidadeambientalurbana.request.dtos.News

// Interface de ponte entre a requisição do azure e a Activity que o usa
interface NewsCallback {

    fun onNewsReceived(news: List<News>)
}