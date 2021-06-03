package com.example.projekskripsi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_detail_aktivitas.*
import kotlinx.android.synthetic.main.activity_detail_pedagang.*
import kotlinx.android.synthetic.main.activity_order_pelanggan.*
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

class detail_aktivitas : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var  adapter: adapterdetailorder
    private  lateinit var  dtnamaproduk : ArrayList<String>
    private  lateinit var  dtjumlah : ArrayList<Int>
    private  lateinit var  dtharga : ArrayList<Int>
    private  lateinit var  dtiddetailorder : ArrayList<Int>
    private  lateinit var  dttotal : ArrayList<Int>
    private var totalhasil = 0
    private  var armenu = arrayListOf<detail_order>()
    private  lateinit var  dtlatitude : ArrayList<Double>
    private  lateinit var  dtlongitude : ArrayList<Double>
    var namapedagang = ""
    var idpedagang = ""
    fun Any.convertRupiah(): String {
        val localId = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localId)
        val strFormat = formatter.format(this)
        return strFormat
    }

    private fun createchatroom(id_order: String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/createchatroom.php?id_order="+id_order
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
                    var msg = respObj.get("err")

                    if(msg.toString() == "2") {
                        val next = Intent(applicationContext, chat::class.java)
                        next.putExtra("id_order", id_order)
                        next.putExtra("idpedagang", idpedagang.toInt())
                        next.putExtra("idpelanggan", MainActivity.id_user_pelanggan!!.toInt())
                        next.putExtra("idchatroom", respObj.get("id_chat_room").toString().toInt())
                        next.putExtra("namapedagang", namapedagang)
                        next.putExtra("tipe",1)
                        startActivity(next)
                    }
                    else if (msg.toString() == "1") {
                        val next = Intent(applicationContext, chat::class.java)
                        next.putExtra("id_order", id_order)
                        next.putExtra("idpedagang", idpedagang.toInt())
                        next.putExtra("idpelanggan", MainActivity.id_user_pelanggan!!.toInt())
                        next.putExtra("idchatroom", respObj.get("id_chat_room").toString().toInt())
                        next.putExtra("namapedagang", namapedagang)
                        next.putExtra("tipe",1)
                        startActivity(next)
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

    private fun getlokasipedagang(id_pedagang: String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getpedagangkelilingbyid.php?id_pedagang="+id_pedagang
        Log.e("url" , url.toString())
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

                    if(msg.toString() == "success") {
                        for (i in 0 until respObj.length() - 1) {
                            val pedagang = respObj.getJSONObject(i.toString())
                            val nama = pedagang.get("nama_lengkap")
                            val latitude = pedagang.get("langitude").toString()
                            val longitude = pedagang.get("longitude").toString()
                            tv_nama_pedagang_detail_aktivitas.setText(nama.toString())
                            namapedagang = nama.toString()
                            dtlatitude.add(latitude.toDouble())
                            dtlongitude.add(longitude.toDouble())
                        }
                        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as? SupportMapFragment
                        mapFragment?.getMapAsync(this)
                        pb_detail_aktivitas.visibility = View.GONE
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
                        var id_pedagang = 0
                        for (i in 0 until respObj.length() - 1) {
                            val listorders = respObj.getJSONObject(i.toString())
                            val iddetail :String = listorders.get("id_detail_order").toString()
                            val harga :Int = listorders.get("harga").toString().toInt()
                            val nama_produk = listorders.get("nama_produk").toString()
                            val jumlah = listorders.get("jumlah").toString().toInt()
                            val total = harga * jumlah
                            id_pedagang = listorders.get("id_pedagang").toString().toInt()
                            subtotal = subtotal + (jumlah * harga)
                            dtiddetailorder.add(iddetail.toInt())
                            dtharga.add(harga)
                            dtnamaproduk.add(nama_produk)
                            dtjumlah.add(jumlah)
                            dttotal.add(total)
                        }
                        for (position in dtnamaproduk.indices){
                            val data = detail_order(
                                dtiddetailorder[position],
                                dtnamaproduk[position],
                                dtjumlah[position],
                                dtharga[position],
                                dttotal[position]
                            )
                            armenu.add(data)

                        }
                        idpedagang = id_pedagang.toString()
                        getlokasipedagang(id_pedagang.toString())
                        val hasil = subtotal.convertRupiah()
                        totalhasil = subtotal
                        tv_total_order.setText(hasil)

                        rv_detail_aktivitas_order.layoutManager  = LinearLayoutManager(this)
                        adapter = adapterdetailorder(armenu,this)
                        rv_detail_aktivitas_order.adapter = adapter
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
    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.apply {
            val user = LatLng(dtlatitude[0], dtlongitude[0])
            addMarker(
                user?.let {
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .position(it)
                        .title(namapedagang)
                }
            )
            moveCamera(CameraUpdateFactory.newLatLngZoom(user, 16.0f));

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_aktivitas)
        kirim_pesan.setOnClickListener{
            val id_order=intent.getStringExtra("id_order")
            createchatroom(id_order.toString())
        }
        val id_order=intent.getStringExtra("id_order")
        pb_detail_aktivitas.visibility = View.VISIBLE
        Log.e("id_order", id_order.toString())
        dtlatitude = arrayListOf()
        dtlongitude = arrayListOf()
        dtnamaproduk = arrayListOf()
        dtjumlah = arrayListOf()
        dtharga = arrayListOf()
        dtiddetailorder = arrayListOf()
        dttotal = arrayListOf()
        getlistorder(id_order.toString())
    }

}