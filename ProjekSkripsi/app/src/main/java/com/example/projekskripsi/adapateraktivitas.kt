package com.example.projekskripsi

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_aktivitas.view.*

class adapateraktivitas(private val listaktivitas: ArrayList<aktivitas_pedagang_keliling>,  private val conten: Context): RecyclerView.Adapter<adapateraktivitas.ListViewHolder>(){
    inner class  ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var txtNama : TextView = itemView.tv_aktivitas
        var imgfoto : ImageView = itemView.iv_foto_aktivitas
        var txtlokasi : TextView = itemView.tv_aktivitas_status
        var imgfotoseatch : ImageView = itemView.iv_foto_aktivitas_detail

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_aktivitas,parent, false)
        return  ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  listaktivitas.size
    }

    override fun onBindViewHolder(holder: adapateraktivitas.ListViewHolder, position: Int) {
        var aktivitas = listaktivitas[position]

        holder.txtNama.setText(aktivitas.nama)
        holder.imgfoto.setImageResource(R.drawable.ic_baseline_perm_identity_24)

        if(aktivitas.status == 1){
            holder.txtlokasi.setText("On Going")
            holder.imgfotoseatch.setImageResource(R.drawable.ic_baseline_search_24)
            holder.imgfotoseatch.setOnClickListener {
                val pIntent2 = Intent (conten,detail_aktivitas::class.java)
                pIntent2.putExtra("id_order",aktivitas.id_order.toString())
                Log.e("id_order", aktivitas.id_order.toString())
                conten.startActivity(pIntent2)
            }
        }
        else if(aktivitas.status ==2){
            holder.txtlokasi.setText("Selesai")
        }


    }
}