package br.com.unip.aps.comunidadeambientalurbana.newsDetails.commentaryModule.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.unip.aps.comunidadeambientalurbana.R
import br.com.unip.aps.comunidadeambientalurbana.environment.Environment
import br.com.unip.aps.comunidadeambientalurbana.request.dtos.Commentary
import com.google.firebase.firestore.FirebaseFirestore

class CommentaryAdapter constructor(): RecyclerView.Adapter<CommentaryViewHolder>() {

    lateinit var context: Context
    lateinit var commentaryList: List<Commentary>
    val firebaseDatabase = FirebaseFirestore.getInstance()

    constructor(context: Context, noticiaUrl: String,commentaryList: List<Commentary>): this() {
        this.context = context
        val collection = noticiaUrl.replace("/", "")
        Log.d("Coleção", collection)
        firebaseDatabase.collection(collection)
            .document(Environment.firestorePath[1])
            .get()
            .addOnSuccessListener { result ->
              Log.d("result", "$result")
            }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentaryViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.commentary_layout,parent,false)
        return CommentaryViewHolder(
            viewHolder
        )
    }

    override fun getItemCount(): Int {
        return commentaryList.size
    }

    override fun onBindViewHolder(holder: CommentaryViewHolder, position: Int) {
        holder.mComentaryUsername.text = commentaryList[position].nome
        holder.mComentary.text = commentaryList[position].commentary
    }
}


class CommentaryViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        var mComentaryUsername = itemView.findViewById<TextView>(R.id.commentaryUserName)
        var mComentary =  itemView.findViewById<TextView>(R.id.commentaryText)

}