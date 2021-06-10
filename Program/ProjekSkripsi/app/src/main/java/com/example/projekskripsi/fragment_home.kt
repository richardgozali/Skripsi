package com.example.projekskripsi

import CustomProgressDialog
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_order_pelanggan.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_menu.*
import org.json.JSONException
import org.json.JSONObject
import android.location.LocationManager as LocationManage


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_home.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_home : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var  adapter: adapaterpedagang_keliling
    private val progressDialog = CustomProgressDialog()

    private  lateinit var  dtnama: ArrayList<String>
    private  lateinit var  dtidpedagang : ArrayList<Int>
    private  lateinit var  dtlokasi: ArrayList<Double>
    private  lateinit var dtlatitude : ArrayList<Double>
    private  lateinit var dtlongitude : ArrayList<Double>

    private  var armenu = arrayListOf<pedagang_keliling>()
    private  var temp = arrayListOf<pedagang_keliling>()
    private var locationManager : LocationManage? = null
    private var location : Location? = null
    private lateinit var map: GoogleMap
    private var coba :Location? = null

    private fun updatetoken(token:String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/updatetoken.php"

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(context!!)

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
                    Log.d("response", respObj.toString())
                    var msg = respObj.get("msg")

                    if(msg.toString() == "error"){

                    }
                    else{

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
                params["id_pelanggan"] = MainActivity.id_user_pelanggan.toString()
                params["token"] = token

                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        queue.add(request)
    }
    private fun getpedagangkeliling(){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getpedagangkeliling.php"

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(context!!)

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
                    Log.d("response", respObj.toString())
                    var msg = respObj.get("msg")

                    val unit = if (msg.toString() == "success") {
                        for (i in 0 until respObj.length() - 1) {
                            val pedagang = respObj.getJSONObject(i.toString())
                            val id_pedagang = pedagang.get("id_pedagang")
                            val telepon = pedagang.get("telepon")
                            val nama_lengkap = pedagang.get("nama_lengkap")
                            val latitude = pedagang.get("langitude")
                            val longitude = pedagang.get("longitude")
                            val r_latitude = pedagang.get("region_latitude")
                            val r_longitude = pedagang.get("region_longitude")
                            Log.d("nama", nama_lengkap.toString())
                            if (location != null) {
                                val jarak = haversine()
                                var akhir = jarak.haversine(
                                    location!!.latitude,
                                    location!!.longitude,
                                    latitude.toString().toDouble(),
                                    longitude.toString().toDouble()
                                )
                                val hasilakhir: Double = akhir
                                val hasil = Math. round(akhir * 10.0) / 10.0
                                if (hasil <= 1.5) {
                                    dtnama.add(nama_lengkap.toString())
                                    dtidpedagang.add(id_pedagang.toString().toInt())
                                    dtlongitude.add(longitude.toString().toDouble())
                                    dtlatitude.add(latitude.toString().toDouble())
                                    dtlokasi.add(hasil.toDouble())
                                }
                            } else {
                                dtlokasi.add(0.0)
                            }

                        }

                        for (position in dtnama.indices) {
                            val data = pedagang_keliling(
                                dtidpedagang[position],
                                dtnama[position],
                                dtlokasi[position]
                            )

                            armenu.add(data)
                        }
                        armenu.sortBy{it.jarak}
                        rv_list_pedagang.layoutManager = LinearLayoutManager(context)
                        adapter = context?.let { adapaterpedagang_keliling(armenu, it) }!!
                        rv_list_pedagang.adapter = adapter
                        progressDialog.dialog.dismiss()
                        val mapFragment =
                            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                        mapFragment?.getMapAsync(callback)
                    } else {


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

    private fun updatelokasi(id_pelanggan: String, longitude: String , latitude:String) {
        // url to post our data
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/updatelokasipelanggan.php"

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(context!!)

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
                    Log.d("response", respObj.toString())
                    var msg = respObj.get("msg")

                    if(msg.toString() == "error"){

                    }
                    else{

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
                params["id_pelanggan"] = id_pelanggan
                params["longitude"] = longitude
                params["latitude"] = latitude

                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        queue.add(request)
    }
    private fun getpedagangkelilingbyname(nama:String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getpedagangkelilingbyname.php?nama_lengkap="+nama
        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(context!!)
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
                    Log.d("response", respObj.toString())
                    var msg = respObj.get("msg")

                    if(msg.toString() == "success"){
                        for (i in 0 until respObj.length() - 1) {
                            val pedagang = respObj.getJSONObject(i.toString())
                            val id_pedagang = pedagang.get("id_pedagang")
                            val telepon = pedagang.get("telepon")
                            val nama_lengkap = pedagang.get("nama_lengkap")
                            val latitude = pedagang.get("langitude")
                            val longitude = pedagang.get("longitude")
                            val r_latitude = pedagang.get("region_latitude")
                            val r_longitude = pedagang.get("region_longitude")
                            Log.d("nama" , nama_lengkap.toString())
                            if(location != null){
                                val jarak = haversine()
                                var akhir = jarak.haversine(location!!.latitude, location!!.longitude,latitude.toString().toDouble(), longitude.toString().toDouble() )
                                val hasil = Math. round(akhir * 10.0) / 10.0
                                dtlokasi.add(hasil)
                                dtnama.add(nama_lengkap.toString())
                                dtidpedagang.add(id_pedagang.toString().toInt())
                                dtlongitude.add(longitude.toString().toDouble())
                                dtlatitude.add(latitude.toString().toDouble())
                            }
                            else{
                                dtlokasi.add(0.0)
                            }

                        }

                        for (position in dtnama.indices) {
                            val data = pedagang_keliling(
                                dtidpedagang[position],
                                dtnama[position],
                                dtlokasi[position]
                            )

                            armenu.add(data)
                        }

                        progressDialog.dialog.dismiss()
                        rv_list_pedagang.layoutManager  = LinearLayoutManager(context)
                        adapter = context?.let { adapaterpedagang_keliling(armenu, it) }!!
                        rv_list_pedagang.adapter = adapter
                        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                        mapFragment?.getMapAsync(callback)
                    }
                    else {


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
    private fun getregionpedagangkeliling(){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getpedagangkeliling.php"
        progressDialog.show(context!!,"Please Wait...")
        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(context!!)

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
                    Log.d("response", respObj.toString())
                    var msg = respObj.get("msg")

                    val unit = if (msg.toString() == "success") {
                        for (i in 0 until respObj.length() - 1) {
                            val pedagang = respObj.getJSONObject(i.toString())
                            val id_pedagang = pedagang.get("id_pedagang")
                            val telepon = pedagang.get("telepon")
                            val nama_lengkap = pedagang.get("nama_lengkap")
                            val latitude = pedagang.get("langitude")
                            val longitude = pedagang.get("longitude")
                            val r_latitude = pedagang.get("region_latitude")
                            val r_longitude = pedagang.get("region_longitude")
                            Log.d("nama", nama_lengkap.toString())
                            if (location != null) {
                                val jarak = haversine()
                                var akhir = jarak.haversine(
                                    location!!.latitude,
                                    location!!.longitude,
                                    r_latitude.toString().toDouble(),
                                    r_longitude.toString().toDouble()
                                )
                                val jarak2 = haversine()
                                val akhir2 = jarak2.haversine(
                                    location!!.latitude,
                                    location!!.longitude,
                                    latitude.toString().toDouble(),
                                    longitude.toString().toDouble()
                                )
                                val hasil = Math. round(akhir * 10.0) / 10.0
                                val hasil2 = Math.round(akhir2 *10.0)/10.0
                                if (hasil <= 1) {
                                    dtnama.add(nama_lengkap.toString())
                                    dtidpedagang.add(id_pedagang.toString().toInt())
                                    dtlongitude.add(longitude.toString().toDouble())
                                    dtlatitude.add(latitude.toString().toDouble())
                                    dtlokasi.add(hasil2.toDouble())
                                }
                            } else {
                                dtlokasi.add(0.0)
                            }

                        }

                        for (position in dtnama.indices) {
                            val data = pedagang_keliling(
                                dtidpedagang[position],
                                dtnama[position],
                                dtlokasi[position]
                            )

                            armenu.add(data)
                        }
                        armenu.sortBy{it.jarak}
                        rv_list_pedagang.layoutManager = LinearLayoutManager(context)
                        adapter = context?.let { adapaterpedagang_keliling(armenu, it) }!!
                        rv_list_pedagang.adapter = adapter
                        progressDialog.dialog.dismiss()
                        val mapFragment =
                            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                        mapFragment?.getMapAsync(callback)
                    } else {


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
    private fun getpedagangterdekat(){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getpedagangkeliling.php"
        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(context!!)

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
                    Log.d("response", respObj.toString())
                    var msg = respObj.get("msg")

                    if(msg.toString() == "success"){
                        for (i in 0 until respObj.length() - 1) {
                            val pedagang = respObj.getJSONObject(i.toString())
                            val id_pedagang = pedagang.get("id_pedagang")
                            val telepon = pedagang.get("telepon")
                            val nama_lengkap = pedagang.get("nama_lengkap")
                            val latitude = pedagang.get("langitude")
                            val longitude = pedagang.get("longitude")
                            val r_latitude = pedagang.get("region_latitude")
                            val r_longitude = pedagang.get("region_longitude")
                            Log.d("nama" , nama_lengkap.toString())
                            if(location != null){
                                val jarak = haversine()
                                var akhir = jarak.haversine(location!!.latitude, location!!.longitude,latitude.toString().toDouble(), longitude.toString().toDouble() )
                                val hasilakhir:Double = String.format("%.1f", akhir).toDouble()
                                dtlokasi.add(hasilakhir)
                                dtnama.add(nama_lengkap.toString())
                                dtidpedagang.add(id_pedagang.toString().toInt())
                                dtlongitude.add(longitude.toString().toDouble())
                                dtlatitude.add(latitude.toString().toDouble())
                            }
                            else{
                                dtlokasi.add(0.0)
                            }

                        }

                        for (position in dtnama.indices) {
                            val data = pedagang_keliling(
                                dtidpedagang[position],
                                dtnama[position],
                                dtlokasi[position]
                            )

                            armenu.add(data)
                        }
                        armenu.sortBy { it.jarak }
                        temp.add(armenu.get(0))
                        val pIntent2 = Intent (context,detail_pedagang::class.java)
                        pIntent2.putExtra("id_pedagang", temp.get(0).id_pedagang_keliling)
                        startActivity(pIntent2)
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
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("latitude Baru",location.latitude.toString())
            var longitude_var = location.longitude.toString()
            var latitude_var : String = location.latitude.toString()
            var id_pelanggans  = MainActivity.id_user_pelanggan
            if (id_pelanggans != null) {
                updatelokasi(id_pelanggans,longitude_var,latitude_var)
                Log.d("masuk","masuk function")
            }
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
    private val callback = OnMapReadyCallback { googleMap ->
        if(location == null){
            val sydney = LatLng(-7.4057672,112.696388)
            latlnguser = sydney
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(sydney)
                            .title("Pedagang Keliling")
                            .snippet("Pedagang Sayur")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.0f));
            Log.d("marker" , sydney.toString())
        }
        else {
            val sydney = location?.latitude?.let { location?.longitude?.let { it1 -> LatLng(it, it1) } }
            googleMap.addMarker(sydney?.let { MarkerOptions().position(it).title("Your's") })
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.0f));
            latlnguser = location?.latitude?.let { location?.longitude?.let { it1 -> LatLng(it, it1) } }
        }

        for (position in dtnama.indices) {
            val latitudes = dtlatitude[position]
            val longitudes =dtlongitude[position]
            val jaraks = dtlokasi[position]
            val jakarta = LatLng(latitudes,longitudes)
            val nama = dtnama[position]
            googleMap.addMarker(
                MarkerOptions()
                    .position(jakarta)
                    .title(nama)
                    .snippet("Berjarak " +jaraks.toString()+ " Km" )
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )

        }
    }
    private val delete =  OnMapReadyCallback { googleMap ->
       googleMap.clear()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog.show(context!!,"Harap Tunggu...")
        getpedagangkeliling()
        dtnama = arrayListOf()
        dtidpedagang = arrayListOf()
        dtlokasi = arrayListOf()
        dtlatitude = arrayListOf()
        dtlongitude = arrayListOf()
        Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());
        val token = FirebaseInstanceId.getInstance().getToken()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            updatetoken(token.toString())
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //fungsi manggil MAPS
        /*val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)*/

        if (this.context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            } !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this.context as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                    this.context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(
                    this.context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
        locationManager = activity!!.getSystemService(LOCATION_SERVICE) as LocationManager
        location  = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        if(location == null){
            location  = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        refresh2.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context!!, R.color.quantum_white_100))
        refresh2.setColorSchemeColors(Color.GREEN)

        refresh2.setOnRefreshListener {
            dtnama = arrayListOf()
            dtidpedagang = arrayListOf()
            dtlokasi = arrayListOf()
            dtlatitude = arrayListOf()
            dtlongitude = arrayListOf()
            armenu = arrayListOf<pedagang_keliling>()
            progressDialog.show(context!!,"Please Wait...")
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment?.getMapAsync(delete)
            getpedagangkeliling()
            refresh2.isRefreshing = false
        }
        search_bar.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{

            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                dtnama = arrayListOf()
                dtidpedagang = arrayListOf()
                dtlokasi = arrayListOf()
                dtlatitude = arrayListOf()
                dtlongitude = arrayListOf()
                armenu = arrayListOf<pedagang_keliling>()
                progressDialog.show(context!!,"Please Wait...")
                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment?.getMapAsync(delete)
                getpedagangkelilingbyname(search_bar.query.toString())
                return false
            }

        })

        btn_filter.setOnClickListener {
            lateinit var dialog:AlertDialog

            // Initialize an array of colors
            val array = arrayOf("Lokasi Pedagang Keliling Berjualan","Pedagang Keliling Terdekat")

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(context!!)

            // Set a title for alert dialog
            builder.setTitle("Pilih Filter.")

            // Set the single choice items for alert dialog with initial selection
            builder.setSingleChoiceItems(array,-1) { _, which ->
                dtnama = arrayListOf()
                dtidpedagang = arrayListOf()
                dtlokasi = arrayListOf()
                dtlatitude = arrayListOf()
                dtlongitude = arrayListOf()
                armenu = arrayListOf<pedagang_keliling>()
                // Get the dialog selected item
                progressDialog.show(context!!,"Please Wait...")
                val filter = array[which]
                Log.e("Filter", filter.toString())
                if (filter == "Lokasi Pedagang Keliling Berjualan") {


                    val mapFragment =
                        childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment?.getMapAsync(delete)
                    getregionpedagangkeliling()
                }
                else{
                    getpedagangterdekat()


                }
                // Dismiss the dialog
                dialog.dismiss()
            }


            // Initialize the AlertDialog using builder object
            dialog = builder.create()

            // Finally, display the alert dialog
            dialog.show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragmen
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_home.
         */
        var latlnguser : LatLng? = null
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            fragment_home().apply {
                arguments = Bundle().apply {

                }
            }


    }
}