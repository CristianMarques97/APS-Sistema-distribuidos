package br.com.unip.aps.comunidadeambientalurbana.request.callBacks

import br.com.unip.aps.comunidadeambientalurbana.request.dtos.Commentary

interface CommentsCallbacks {
        fun volleyResponse(commentsList: MutableList<Commentary>)
}