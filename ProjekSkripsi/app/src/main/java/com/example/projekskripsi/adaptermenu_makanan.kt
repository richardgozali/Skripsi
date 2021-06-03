package com.example.projekskripsi

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_menu.view.*

class adaptermenu_makanan (private val listmenumakanan : ArrayList<menu_makanan>, private  val conten : Context): RecyclerView.Adapter<adaptermenu_makanan.ListViewHolder>(){
    inner class  ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var txtNama : TextView = itemView.tv_nama
        var imgfoto : ImageView = itemView.iv_foto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_menu,parent, false)
        return  ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  listmenumakanan.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       var menu = listmenumakanan[position]
        holder.txtNama.setText(menu.nama)
        Picasso.get().load(menu.foto).into(holder.imgfoto)
        holder.txtNama.setOnClickListener {
            val pIntent2 = Intent (conten,detail_menu_masakan::class.java)
            pIntent2.putExtra("id_masakan",menu.id_menu)
            conten.startActivity(pIntent2)

        }
    }
}