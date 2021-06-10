package com.example.projekskripsi

import CustomProgressDialog
import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_home_pedagang.*
import org.json.JSONException
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_home_pedagang.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_home_pedagang : Fragment() {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //libary lokasi dan variable lokasi
    private var locationManager : LocationManager? = null
    private var location : Location? = null
    private val progressDialog = CustomProgressDialog()
    private lateinit var  adapter: adapterproduk

    private  lateinit var  dtnama_produk: ArrayList<String>
    private  lateinit var  dtfoto : ArrayList<String>
    private  lateinit var  dt_harga: ArrayList<Int>
    private  lateinit var  dt_jumlah: ArrayList<Int>
    private  lateinit var  dt_tipe: ArrayList<Int>
    private  lateinit var  dtsatuan: ArrayList<String>
    private  lateinit var  dtjenis: ArrayList<String>

    private  var arproduk = arrayListOf<produk>()
    private var hasjenis: HashMap<Int,String > = HashMap<Int,String>()

    //fungsi update lokasi menggunakan API
    private fun updatelokasi(id_pedagang:String, longitude:String, latitude:String){

        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/updatelokasipedagang.php"

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
    private fun getprodukpedagang(){
        val id_pedagang = MainActivity.id_user_pedagang
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getprodukbyid_pedagang.php?id_pedagang="+id_pedagang
        Log.e("url" , url.toString())
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
                    var msg = respObj.get("msg")
                    var tipe = 2
                    var temp = ""
                    if(msg.toString() == "success"){
                        Log.d("masuk" , "coba")
                        for (i in 0 until respObj.length()-1) {
                            val produk = respObj.getJSONObject(i.toString())
                            val nama_produk = produk.get("nama_produk")
                            val id_produk = produk.get("id_produk")
                            val foto = produk.get("img_produk")
                            val jumlah  = produk.get("jumlah")
                            val harga = produk.get("harga").toString().toInt()
                            val unit =produk.get("unit").toString()
                            val jenis = produk.get("id_jenis").toString().toInt()
                            val key = hasjenis.get(jenis)

                            if(temp == key!!){
                                tipe = 1
                            }
                            else{
                                tipe =2
                                temp = key!!
                            }
                            dtjenis.add(key!!)
                            dtnama_produk.add(nama_produk.toString())
                            dtfoto.add(foto.toString())
                            dt_jumlah.add(jumlah.toString().toInt())
                            dtsatuan.add(unit)
                            dt_harga.add(harga)
                            dt_tipe.add(tipe)
                        }

                        for (position in dtnama_produk.indices){
                            val data = produk(
                                dtfoto[position],
                                dtnama_produk[position],
                                dt_jumlah[position],
                                dtsatuan[position],
                                dt_harga[position],
                                dtjenis[position],
                                dt_tipe[position]

                            )

                            arproduk.add(data)
                            Log.d("array_produk", arproduk.toString())
                            rv_produk.layoutManager  = LinearLayoutManager(context)
                            adapter = adapterproduk(arproduk)
                            rv_produk.adapter = adapter
                            pb_fragment_home.visibility = View.GONE
                            linearLayout_fragment_home.visibility = View.VISIBLE
                        }
                    }
                    else{

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    pb_fragment_home.visibility = View.GONE
                    linearLayout_fragment_home.visibility = View.VISIBLE
                }
            },
            Response.ErrorListener { error -> // method to handle errors.

            }) {

        }
        // below line is to make
        // a json object request.
        queue.add(request)
    }
    private fun getjenisproduk(){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getjenis.php"

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
                        getprodukpedagang()
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
    private fun updatetoken(token : String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/updatetokenpedagang.php"

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
                params["id_pedagang"] = MainActivity.id_user_pedagang.toString()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     
        dtnama_produk = arrayListOf()
        dt_harga = arrayListOf()
        dtsatuan = arrayListOf()
        dtfoto = arrayListOf()
        dt_jumlah = arrayListOf()
        dtjenis = arrayListOf()
        dt_tipe = arrayListOf()
        getjenisproduk()

        //request code untuk mengakases lokasi
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
        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        location  = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        if(location == null){
            location  = locationManager?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        }
    }

    //location listener untuk mengetahui kapan device bergerak
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("latitude Baru",location.latitude.toString())
            val id_user_pedagang = MainActivity.id_user_pedagang
            if(location.latitude != null){
                updatelokasi(id_user_pedagang.toString(), location.longitude.toString(),location.latitude.toString())
                lokasi = location
            }


        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pb_fragment_home.visibility = View.VISIBLE
        linearLayout_fragment_home.visibility = View.GONE
        btn_tambah.setOnClickListener {
            val fragment = customdialog()
            fragment.show(childFragmentManager,"DemoFragment")
        }
        val token = FirebaseInstanceId.getInstance().getToken()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            updatetoken(token.toString())
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_pedagang, container, false)
    }

    companion object {
        private var lokasi : Location? = null

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_home_pedagang.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            fragment_home_pedagang().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}