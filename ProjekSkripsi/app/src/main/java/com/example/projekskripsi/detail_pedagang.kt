package com.example.projekskripsi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_detail_pedagang.*
import org.json.JSONException
import org.json.JSONObject


class detail_pedagang : AppCompatActivity(), OnMapReadyCallback {
    fun reload(){
        getjenisproduk(id_pedagang.toString())
    }
    companion object{
        var id_pedagang = 0
        fun refresh(){

        }
    }


    private lateinit var  adapter: adapterdetail_pedagang
    private  lateinit var  dtnama: ArrayList<String>
    private  lateinit var  dtfoto : ArrayList<String>
    private  lateinit var  dtharga : ArrayList<Int>
    private  lateinit var  dtjumlah : ArrayList<Int>
    private  lateinit var  dtkategori : ArrayList<String>
    private  lateinit var  dtunit : ArrayList<String>
    private  lateinit var  dtlatitude : ArrayList<Double>
    private  lateinit var  dtlongitude : ArrayList<Double>
    private  lateinit var  dtidproduk :ArrayList<Int>
    private  lateinit var  namapedagang : String
    private  lateinit var  idorder:String
    private  lateinit var  dt_tipe :ArrayList<Int>

    private  var armenu = arrayListOf<produk_detail>()
    private var hasjenis: HashMap<Int,String > = HashMap<Int,String>()

    private fun getjenisproduk(id_pedagang: String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getjenis.php"

        // creating a new variable for our request queue
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

                    if(msg.toString() == "error"){

                    }
                    else{

                        for (i in 0 until respObj.length()-1) {
                            val jenis = respObj.getJSONObject(i.toString())
                            val nama_jenis = jenis.get("nama_jenis")
                            val id_jenis:Int = jenis.get("id_jenis").toString().toInt()
                            val keterangan = jenis.get("keterangan")
                            hasjenis.put( id_jenis,nama_jenis.toString())
                            Log.d("jenis" , id_jenis.toString())
                        }
                        getprodukpedagang(id_pedagang)
                        getjumlahkeranjang(id_pedagang)
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
                            namapedagang = nama.toString()
                            dtlatitude.add(latitude.toDouble())
                            dtlongitude.add(longitude.toDouble())
                        }
                        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
                        mapFragment?.getMapAsync(this)
                        pb_detail_pedagang.visibility = View.GONE
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
    private fun getprodukpedagang(id_pedagang:String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getprodukbyid_pedagang.php?id_pedagang="+id_pedagang
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
                    var tipe = 2
                    var temp = ""
                    if(msg.toString() == "success") {
                        for (i in 0 until respObj.length() - 1) {
                            val produk = respObj.getJSONObject(i.toString())
                            val nama_produk = produk.get("nama_produk")
                            val id_produk = produk.get("id_produk_pedagang")
                            val foto = produk.get("img_produk")
                            val jumlah = produk.get("jumlah")
                            val harga = produk.get("harga").toString().toInt()
                            val unit = produk.get("unit").toString()
                            val jenis = produk.get("id_jenis").toString().toInt()
                            val key = hasjenis.get(jenis)
                            if(temp == key!!){
                                tipe = 1

                            }
                            else{
                                tipe =2
                                temp = key!!
                            }
                            dtfoto.add(foto.toString())
                            dtharga.add(harga.toInt())
                            dtnama.add(nama_produk.toString())
                            dtkategori.add(key.toString())
                            dtjumlah.add(jumlah.toString().toInt())
                            dtidproduk.add(id_produk.toString().toInt())
                            dt_tipe.add(tipe)
                            dtunit.add(unit.toString())
                            Log.e("dtipe", dtfoto.toString())
                        }
                        for (position in dtnama.indices){
                            val data = produk_detail(
                                dtidproduk[position],
                                dtfoto[position],
                                dtnama[position],
                                dtjumlah[position],
                                dtharga[position],
                                dtkategori[position],
                                dtunit[position],
                                dt_tipe[position]
                            )

                            armenu.add(data)

                        }
                        rv_detail_pedagang.layoutManager  = LinearLayoutManager(this)
                        adapter = adapterdetail_pedagang(armenu,MainActivity.id_user_pelanggan!!, id_pedagang,this)
                        rv_detail_pedagang.adapter = adapter

                        getlokasipedagang(id_pedagang.toString())

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
    fun getjumlahkeranjang(id_pedagang:String){

        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getjumlahkeranjang.php"
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
                        btn_keranjang.setText(respObj.get("count").toString())
                        idorder = respObj.get("id_order").toString()
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
                params["id_pedagang"] = id_pedagang
                params["id_pelanggan"] = MainActivity.id_user_pelanggan.toString()
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
        idorder = "0"
        setContentView(R.layout.activity_detail_pedagang)
        dtlongitude = arrayListOf()
        dtlatitude = arrayListOf()
        dtfoto = arrayListOf()
        dtjumlah = arrayListOf()
        dtkategori = arrayListOf()
        dtnama = arrayListOf()
        dtharga = arrayListOf()
        dtidproduk = arrayListOf()
        dt_tipe= arrayListOf()
        dtunit = arrayListOf()
        pb_detail_pedagang.visibility = View.VISIBLE
        iv_back.setOnClickListener{
            onBackPressed()
        }
        if(id_pedagang == 0){
            id_pedagang = intent.getIntExtra("id_pedagang", 0)
        }

        getjenisproduk(id_pedagang.toString())
        btn_keranjang.setOnClickListener {
            Log.d("id_order" , idorder)
            if(idorder == "0"){
                getjenisproduk(id_pedagang.toString())
            }
            else {
                val next = Intent(applicationContext, order_pelanggan::class.java)
                next.putExtra("id_order", idorder)
                next.putExtra("id_pedagang", id_pedagang)
                Log.e("id_pedagang", id_pedagang.toString())
                startActivity(next)
            }
        }

    }

    override fun onStop() {
        id_pedagang = 0
        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.apply {
            val user = LatLng(dtlatitude[0], dtlongitude[0])
            addMarker(
                user?.let {
                    MarkerOptions()
                        .position(it)
                        .title(namapedagang)
                }
            )
            moveCamera(CameraUpdateFactory.newLatLngZoom(user, 15.0f));

        }
    }
}