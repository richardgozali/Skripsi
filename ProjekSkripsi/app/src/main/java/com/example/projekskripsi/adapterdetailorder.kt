package com.example.projekskripsi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_detail_order.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class adapterdetailorder(private val listdetailorder : ArrayList<detail_order>, private val conten: Context): RecyclerView.Adapter<adapterdetailorder.ListViewHolder>(){
    inner class  ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var nama_produk : TextView = itemView.tv_list_detail_order
        var jumlah : TextView = itemView.tv_jumlah_detail_order
        var harga : TextView = itemView.tv_detail_order_subtotal
        var subtotal : TextView = itemView.tv_detail_order_total
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_order,parent, false)
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