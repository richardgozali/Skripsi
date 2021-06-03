package com.example.projekskripsi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_detail_pedagang.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_order_pelanggan.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class order_pelanggan : AppCompatActivity() {
    private lateinit var  adapter: adapterlistorder
    private  lateinit var  dtnamaproduk : ArrayList<String>
    private  lateinit var  dtjumlah : ArrayList<Int>
    private  lateinit var  dtharga : ArrayList<Int>
    private  lateinit var  dtiddetailorder : ArrayList<Int>
    private  lateinit var  dttotal : ArrayList<Int>
    private var totalhasil = 0
    private  var armenu = arrayListOf<listorder>()

    fun Any.convertRupiah(): String {
        val localId = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localId)
        val strFormat = formatter.format(this)
        return strFormat
    }
    private  fun getlistorder(id_order:String){
        val url ="https://tos.petra.ac.id/~c14170049/Skripsi/getlistorder.php?id_order=" + id_order
        Log.e("URL :", url)
        val queue = Volley.newRequestQueue(this)

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.

        val request: StringRequest = object : StringRequest(
            Request.Method.GET,
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
                    var msg = respObj.get("msg")
                    var subtotal  =0
                    if(msg.toString() == "success") {

                        for (i in 0 until respObj.length() - 1) {
                            val listorders = respObj.getJSONObject(i.toString())
                            val iddetail :String = listorders.get("id_detail_order").toString()
                            val harga :Int = listorders.get("harga").toString().toInt()
                            val nama_produk = listorders.get("nama_produk").toString()
                            val jumlah = listorders.get("jumlah").toString().toInt()
                            val total = harga * jumlah
                            subtotal = subtotal + (jumlah * harga)
                            dtiddetailorder.add(iddetail.toInt())
                            dtharga.add(harga)
                            dtnamaproduk.add(nama_produk)
                            dtjumlah.add(jumlah)
                            dttotal.add(total)
                        }
                        for (position in dtnamaproduk.indices){
                            val data = listorder(
                                dtiddetailorder[position],
                                dtnamaproduk[position],
                                dtjumlah[position],
                                dtharga[position],
                                dttotal[position]
                            )
                            armenu.add(data)

                        }
                        val hasil = subtotal.convertRupiah()
                        totalhasil = subtotal
                        _tv_total.setText(hasil)
                        rv_detail_order.layoutManager  = LinearLayoutManager(this)
                        adapter = adapterlistorder(armenu,this)
                        rv_detail_order.adapter = adapter
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            },
            Response.ErrorListener { error -> // method to handle errors.

            }) {

        }
        // below line is to make
        // a json object request.
        queue.add(request)


    }
    private fun sendnotif(id_pedagang:String,id_order :String,totalhasil : Int){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/sendnotif.php"
        val queue = Volley.newRequestQueue(this)
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
                    Log.e("coba", respObj.toString())
                    // below are the strings which we
                    // extract from our json object.
                    updateorder(id_order,totalhasil)

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
                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        queue.add(request)
    }
    private  fun updateorder(id_order: String,total :Int){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/updateorder.php"
        val queue = Volley.newRequestQueue(this)
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
                    var msg = respObj.get("msg")
                    if(msg =="success"){

                        val builder = AlertDialog.Builder(this, R.style.Alertlogin)
                        builder.setTitle("Success")
                        builder.setMessage("PRODUK TELAH TERPESAN HARAP MENUNGGU")
                        builder.setPositiveButton("OK", null)
                        builder.show()
                        val next = Intent(applicationContext, home::class.java)
                        startActivity(next)
                    }

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
                params["id_order"] = id_order
                params["total"] = total.toString()
                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        queue.add(request)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_pelanggan)
        val id_order=intent.getStringExtra("id_order")
        val id_pedagang = intent.getIntExtra("id_pedagang",0)
        Log.e("id_pedagang", id_pedagang.toString())
        dtnamaproduk = arrayListOf()
        dtjumlah = arrayListOf()
        dtharga = arrayListOf()
        dtiddetailorder = arrayListOf()
        dttotal = arrayListOf()
        getlistorder(id_order.toString())
        btn_beli.setOnClickListener {
            sendnotif(id_pedagang.toString(),id_order!!,totalhasil)
        }

    }
}