package com.example.projekskripsi


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_header_produk.view.*
import kotlinx.android.synthetic.main.item_produk.view.*

private  var kategori = ""
private  var viewType = 2

class adapterproduk(private val listproduk : ArrayList<produk>): RecyclerView.Adapter<adapterproduk.ViewHolder>(){
    companion object{
        val ITEM_A = 1
        val ITEM_B = 2
    }
    inner class ViewHolderItemA(itemView: View) : ViewHolder(itemView) {
        var txtNama : TextView = itemView.tv_nama_produk
        var imgfoto : ImageView = itemView.iv_produk
        var jumlah : TextView = itemView.et_jumlah_produk
        var harga : TextView = itemView.et_harga
    }

    inner class ViewHolderItemB(itemView: View) : ViewHolder(itemView){
        var txtNama : TextView = itemView.tv_nama_produk2
        var imgfoto : ImageView = itemView.iv_produk2
        var jumlah : TextView = itemView.et_jumlah_produk2
        var harga : TextView = itemView.et_harga2
        var judul :TextView = itemView.title_header
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_A -> {
                ViewHolderItemA(inflater.inflate(R.layout.item_produk,parent, false))
            }
            else -> ViewHolderItemB(inflater.inflate(R.layout.item_header_produk,parent, false))
        }
    }

    override fun getItemCount(): Int {
        return  listproduk.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = listproduk[position].tipe
        when (viewType) {
            ITEM_A -> {
                val viewHolderA = holder as ViewHolderItemA
                var produk = listproduk[position]
                viewHolderA.txtNama.text =(produk.nama_produk)
                Picasso.get().load(produk.foto).into(viewHolderA.imgfoto)
                viewHolderA.jumlah.text =(produk.satuan)
                viewHolderA.harga.text =("Rp " + produk.harga.toString()+ "/" +produk.satuan)
                Log.e("tipe1", "masuk")
            }
            else ->{
                val viewHolderB = holder as ViewHolderItemB
                var produk = listproduk[position]
                viewHolderB.txtNama.setText(produk.nama_produk)
                Picasso.get().load(produk.foto).into(viewHolderB.imgfoto)
                viewHolderB.jumlah.setText(produk.satuan)
                viewHolderB.harga.setText("Rp " + produk.harga.toString()+ "/" +produk.satuan)
                viewHolderB.judul.setText(produk.jenis)
                Log.e("tipe2", "masuk")

            }

        }

    }
    override fun getItemViewType(position: Int): Int = listproduk[position].tipe
    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}