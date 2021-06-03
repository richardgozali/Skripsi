package com.example.projekskripsi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_chat_me.view.*
import kotlinx.android.synthetic.main.item_chat_yours.view.*
import kotlinx.android.synthetic.main.item_pedagang_keliling.view.*

class adapterchat(private val listchats : ArrayList<chats>) : RecyclerView.Adapter<adapterchat.ViewHolder>() {
    companion object{
        val ITEM_A = 1
        val ITEM_B = 2
    }
    inner class ViewHolderItemA(itemView: View) : ViewHolder(itemView) {
        val cardme : CardView = itemView.card_me
        val textView: TextView = itemView.findViewById(R.id.tv_chat_me)
    }

    inner class ViewHolderItemB(itemView: View) : ViewHolder(itemView){
        var cardyours: CardView = itemView.card_chat_yours
        val pesan: TextView = itemView.findViewById(R.id.tv_chat_your)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_A -> {
                ViewHolderItemA(inflater.inflate(R.layout.item_chat_me,parent, false))
            }
            else -> ViewHolderItemB(inflater.inflate(R.layout.item_chat_yours,parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = listchats[position].tipe
        when (viewType) {
            ITEM_A -> {
                val viewHolderA = holder as ViewHolderItemA
                viewHolderA.textView.text = listchats[position].message
            }
            else -> {
                // Lakukan sesuatu jika kamu ingin mengubah gambar pada ViewHolderItemB
                val viewHolderB = holder as ViewHolderItemB
                viewHolderB.pesan.text = listchats[position].message
            }
        }
    }

    override fun getItemCount(): Int = listchats.size

    override fun getItemViewType(position: Int): Int = listchats[position].tipe

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)



}
