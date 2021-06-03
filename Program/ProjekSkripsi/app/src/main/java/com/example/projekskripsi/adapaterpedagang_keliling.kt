package com.example.projekskripsi

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.item_pedagang_keliling.view.*

class adapaterpedagang_keliling(private val listpedangangkeliling: ArrayList<pedagang_keliling>, private val conten: Context): RecyclerView.Adapter<adapaterpedagang_keliling.ListViewHolder>(){
    companion object {
        var id_pedagang  = 0
    }
    inner class  ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var txtNama : TextView = itemView.tv_nama_pedagang_keliling
        var imgfoto : ImageView = itemView.iv_foto_pedagang_keliling
        var txtlokasi : TextView = itemView.tv_jarak_pedagang_keliling
        var imgfotoseatch : ImageView = itemView.iv_foto_search

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_pedagang_keliling,parent, false)
        return  ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  listpedangangkeliling.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var pedagang = listpedangangkeliling[position]
        holder.txtNama.setText(pedagang.nama)
        holder.imgfoto.setImageResource(R.drawable.ic_baseline_perm_identity_24)
        holder.txtlokasi.setText(pedagang.jarak.toString()+" Km")
        holder.imgfotoseatch.setImageResource(R.drawable.ic_baseline_search_24)

        holder.imgfotoseatch.setOnClickListener {
            id_pedagang = pedagang.id_pedagang_keliling
            val pIntent2 = Intent (conten,detail_pedagang::class.java)
            pIntent2.putExtra("id_pedagang", id_pedagang)
            conten.startActivity(pIntent2)
        }
    }
}