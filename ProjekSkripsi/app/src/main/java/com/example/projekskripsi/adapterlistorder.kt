package com.example.projekskripsi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_order.view.*
import kotlinx.android.synthetic.main.item_produk.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class adapterlistorder(private val listdetailorder : ArrayList<listorder>, private val conten: Context): RecyclerView.Adapter<adapterlistorder.ListViewHolder>(){
    inner class  ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var nama_produk : TextView = itemView.tv_list_order
        var jumlah : TextView = itemView.tv_jumlah
        var harga : TextView = itemView.tv_subtotal
        var subtotal : TextView = itemView.tv_total
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_order,parent, false)
        return  ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  listdetailorder.size
    }
    fun Any.convertRupiah(): String {
        val localId = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localId)
        val strFormat = formatter.format(this)
        return strFormat
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var order = listdetailorder[position]
        holder.nama_produk.setText(order.nama_produk)
        holder.jumlah.setText(order.jumlah.toString() + "X")
        val hasil = order.harga.convertRupiah()
        holder.subtotal.setText(order.subtotal.convertRupiah())
        holder.harga.setText(hasil)
    }
}