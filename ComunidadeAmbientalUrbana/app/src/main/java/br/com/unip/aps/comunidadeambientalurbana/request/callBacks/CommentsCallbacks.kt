package br.com.unip.aps.comunidadeambientalurbana.request.callBacks

import br.com.unip.aps.comunidadeambientalurbana.request.dtos.Commentary

interface CommentsCallbacks {
        fun onCommentaryReceived(commentsList: MutableList<Commentary>)
        fun onCommentaryListChanged(comment: MutableMap<String, Any>)
        fun onCommentaryAdd()
}