package br.com.unip.aps.comunidadeambientalurbana.request.callBacks

// Interface de ponte entre as requisições do firebase e a Activity que o utiliza
interface CommentsCallbacks {
        fun onCommentaryListChanged(comment: MutableMap<String, Any>)
        fun onCommentaryAdd()
}