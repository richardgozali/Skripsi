package com.example.projekskripsi

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.item_pedagang_detail.view.*

import kotlinx.android.synthetic.main.item_pedagang_detail_header.view.*

import org.json.JSONException
import org.json.JSONObject



class adapterdetail_pedagang(private val listprodukdetail : ArrayList<produk_detail>,idpelanggan:String,idpedagang:String,context: Context) :
    RecyclerView.Adapter<adapterdetail_pedagang.ViewHolder>(){
    val id_pelanggan = idpelanggan
    val id_pedagang = idpedagang
    val conten = context
    companion object{
        val ITEM_A = 1
        val ITEM_B = 2
    }
    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class ViewHolderItemA(itemView: View) : ViewHolder(itemView) {
        var txtNama : TextView = itemView.tv_nama_produk_detail
        var imgfoto : ImageView = itemView.iv_pedagang_detail
        var jumlah : TextView = itemView.et_jumlah_produk_detail
        var harga : TextView = itemView.et_harga_detil
        var beli : Button = itemView.btn_add_chart

        var sisaproduk : TextView = itemView.tv_sisa_produk
    }

    inner class ViewHolderItemB(itemView: View) : ViewHolder(itemView){
        var txtNama : TextView = itemView.tv_nama_produk_detail_header

        var imgfoto : ImageView = itemView.iv_pedagang_detail_header
        var jumlah : TextView = itemView.et_jumlah_produk_detai_header
        var harga : TextView = itemView.et_harga_detil_header
        var beli : Button = itemView.btn_add_chart_header
        var header : TextView = itemView.title_detail_header
        var sisaproduk : TextView = itemView.tv_sisa_produk_header
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return  when (viewType) {
            ITEM_A -> {
                ViewHolderItemA(inflater.inflate(R.layout.item_pedagang_detail,parent,false))

            }
            else -> ViewHolderItemB(inflater.inflate(R.layout.item_pedagang_detail_header,parent, false))
        }

    }
    override fun getItemCount(): Int {
        return  listprodukdetail.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = listprodukdetail[position].tipe
        when (viewType) {
            ITEM_A -> {

                val viewHolderA = holder as adapterdetail_pedagang.ViewHolderItemA


                var produk = listprodukdetail[position]

                var harga = produk.harga
                var jumlah : Int = 0
                var temp : String = ""

                viewHolderA.txtNama.setText(produk.nama)
                Picasso.get().load(produk.foto).into(viewHolderA.imgfoto)
                viewHolderA.sisaproduk.setText("<" + produk.jumlah)
                viewHolderA.harga.setText("Rp "+produk.harga.toString() +"/ " + produk.unit)

                viewHolderA.beli.setOnClickListener {
                    temp = holder.jumlah.text.toString()
                    jumlah = temp.toInt()
                    var subtotal = jumlah * harga
                    holder.jumlah.setText(0.toString())
                    Log.d("Subtotal" , subtotal.toString())
                    tambahorder(id_pelanggan,id_pedagang,produk.idproduk.toString(),harga.toString(),jumlah.toString())

                }
                Log.e("tipe1", "masuk")
            }
            else ->{
                val viewHolderB = holder as adapterdetail_pedagang.ViewHolderItemB
                var produk = listprodukdetail[position]

                var harga = produk.harga
                var jumlah : Int = 0
                var temp : String = ""
                viewHolderB.txtNama.setText(produk.nama)
                Picasso.get().load(produk.foto).into(viewHolderB.imgfoto)
                viewHolderB.sisaproduk.setText("<" + produk.jumlah)
                viewHolderB.harga.setText("Rp "+produk.harga.toString()+"/ " + produk.unit)
                viewHolderB.header.setText(produk.kategori)
                viewHolderB.beli.setOnClickListener {
                    temp = holder.jumlah.text.toString()
                    jumlah = temp.toInt()
                    var subtotal = jumlah * harga
                    holder.jumlah.setText(0.toString())
                    Log.d("Subtotal" , subtotal.toString())
                    tambahorder(id_pelanggan,id_pedagang,produk.idproduk.toString(),harga.toString(),jumlah.toString())

                }
            }

        }
    }

    private  fun tambahorder(id_pelanggan:String , id_pedagang:String,idproduk:String,harga:String,jumlah:String){

        val url ="https://tos.petra.ac.id/~c14170049/Skripsi/insertorder.php"
        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(conten)
        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.

        val request: StringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            Response.Listener { response ->
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                // on below line we are displaying a success toast message.

                try {
                    // on below line we are passing our response
                    // to json object to extract data from it.
                    val respObj = JSONObject(response)

                    // below are the strings which we
                    // extract from our json object.

                    AlertDialog.Builder(conten)
                        // Judul
                        .setTitle("Perhatian")
                        // Pesan yang di tamopilkan
                        .setMessage("Data Telah Ditambah")
                        .show()
                        .dismiss()

                    val pIntent2 = Intent (conten,detail_pedagang::class.java)
                    pIntent2.putExtra("id_pedagang",id_pedagang.toInt())
                    pIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    AlertDialog.Builder(conten).setOnDismissListener {

                    }
                    conten.startActivity(pIntent2)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> // method to handle errors.

            }) {
            override fun getParams(): Map<String, String> {
                // below line we are creating a map for
                // storing our values in key and value pair.
                val params: MutableMap<String, String> =
                    HashMap()

                // on below line we are passing our key
                // and value pair to our parameters.
                params["id_pedagang"] = id_pedagang
                params["id_pelanggan"] = id_pelanggan
                params["id_produk_pedagang"] = idproduk
                params["harga"] = harga
                params["jumlah"] = jumlah
                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        queue.add(request)

    }

    override fun getItemViewType(position: Int): Int = listprodukdetail[position].tipe

}