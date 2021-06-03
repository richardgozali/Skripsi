package com.example.projekskripsi

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_aktivitas_pedagang.view.*
import kotlinx.android.synthetic.main.item_pedagang_keliling.view.*

class adapaterpelanggan(private val listpelanggan : ArrayList<pelanggan>, private val conten: Context): RecyclerView.Adapter<adapaterpelanggan.ListViewHolder>(){
    inner class  ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imgfoto : ImageView = itemView.iv_foto_aktivitas_pelanggan
        var txtnama : TextView = itemView.tv_aktivitas_pelanggan
        var txtlokasi : TextView = itemView.tv_aktivitas_status_pelanggan
        var imgfotoseatch : ImageView = itemView.iv_foto_aktivitas_detail_pelanggan

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_aktivitas_pedagang,parent, false)
        return  ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  listpelanggan.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var pelanggan = listpelanggan[position]
        holder.txtnama.setText(pelanggan.nama)
        holder.imgfoto.setImageResource(R.drawable.ic_baseline_perm_identity_24)

        if(pelanggan.jarak == 1){
            holder.txtlokasi.setText("On Going")
        }
        else if(pelanggan.jarak ==2){
            holder.txtlokasi.setText("Done")
        }

        holder.imgfotoseatch.setImageResource(R.drawable.ic_baseline_search_24)
        holder.imgfotoseatch.setOnClickListener {
            val pIntent2 = Intent (conten,detailorderpedagang::class.java)
            pIntent2.putExtra("id_order",pelanggan.id_order)
            pIntent2.putExtra("id_pelanggan", pelanggan.id_pelanggan)
            pIntent2.putExtra("status", pelanggan.jarak)
            conten.startActivity(pIntent2)
        }
    }
}