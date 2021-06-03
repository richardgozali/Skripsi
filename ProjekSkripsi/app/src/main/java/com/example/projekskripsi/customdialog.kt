package com.example.projekskripsi
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.custom_dialog.*
import org.json.JSONException
import org.json.JSONObject

private  lateinit var  arrayproduk: ArrayList<String>
private  lateinit var  arrayunit: Array<String>
private  lateinit var  arrayjenis : ArrayList<String>
private var hasjenis: HashMap<String,Int > = HashMap<String,Int>()
private  var hasproduk :  HashMap<String,Int > = HashMap<String,Int>()

class customdialog: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
        val view = inflater.inflate(R.layout.custom_dialog, container, false)

        return view

        // Initializing a String Array

    }
    private  fun getjenisproduk(){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getjenis.php"

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(context!!)
        arrayjenis = arrayListOf()
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
                    Log.d("response jenis", respObj.toString())
                    var msg = respObj.get("msg")

                    if(msg.toString() == "error"){

                    }
                    else{
                        for (i in 0 until respObj.length()-1) {
                            val jenis = respObj.getJSONObject(i.toString())
                            val nama_jenis = jenis.get("nama_jenis")
                            val id_jenis:Int = jenis.get("id_jenis").toString().toInt()
                            val keterangan = jenis.get("keterangan")

                            arrayjenis.add(nama_jenis.toString())
                            hasjenis.put(nama_jenis.toString(), id_jenis)
                            Log.d("jenis" , id_jenis.toString())
                          }
                        Log.d("array jenis" , arrayjenis.toString())
                        var jenis_spinner : Spinner = spinner_jenis
                        jenis_spinner.adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, arrayjenis)

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
    private  fun getproduk(){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getproduk.php"

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(context!!)
        arrayjenis = arrayListOf()
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
                    Log.d("response produk", respObj.toString())
                    var msg = respObj.get("msg")

                    if(msg.toString() == "error"){

                    }
                    else{
                        for (i in 0 until respObj.length()-1) {
                            val produk = respObj.getJSONObject(i.toString())
                            val nama_produk = produk.get("nama_produk")
                            val id_produk = produk.get("id_produk")
                            hasproduk.put(nama_produk.toString() , id_produk.toString().toInt())
                            arrayproduk.add(nama_produk.toString())
                        }
                        Log.d("array produk" , arrayproduk.toString())
                        val spinner: Spinner = spinner_judul_produk
                        spinner.adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, arrayproduk)

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
    private  fun  getprodukbyid(id_jenis : String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getprodukbyid.php?id_jenis="+id_jenis
        Log.e("url", url.toString())

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
                    Log.d("response by id", respObj.toString())
                    Log.e("Jumlah Objek",respObj.length().toString())
                    var msg = respObj.get("msg")

                    if(msg.toString() == "error"){

                    }
                    else if ( msg.toString() == ""){
                        arrayproduk = arrayListOf()
                        val spinner: Spinner = spinner_judul_produk
                        spinner.adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, arrayproduk)
                    }
                    else{
                        arrayproduk = arrayListOf()

                        for (i in 0 until respObj.length()-1) {
                            val produk = respObj.getJSONObject(i.toString())
                            val nama_produk = produk.get("nama_produk")
                            val id_produk = produk.get("id_produk")
                            hasproduk.put(nama_produk.toString() , id_produk.toString().toInt())
                            arrayproduk.add(nama_produk.toString())
                        }
                        Log.d("array produk" , arrayproduk.toString())
                        val spinner: Spinner = spinner_judul_produk
                        spinner.adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, arrayproduk)

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
    private  fun tambahprodukpedagang(idpedagang:String,idproduk:String,price:String,jumlah:String,unit:String){
        var url = "https://tos.petra.ac.id/~c14170049/Skripsi/insertprodukpedagang.php"

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
                et_harga_dialog.setText("");
                et_jumlah_dialog.setText("");

                // on below line we are displaying a success toast message.

                try {
                    // on below line we are passing our response
                    // to json object to extract data from it.
                    val respObj = JSONObject(response)

                    // below are the strings which we
                    // extract from our json object.
                    Log.d("response", respObj.toString())
                    var msg = respObj.get("msg")

                    if(msg.toString() == "Already INSERT to Database"){
                        val builder = androidx.appcompat.app.AlertDialog.Builder(context!!, R.style.Alertlogin)
                        builder.setTitle("Peringatan")
                        builder.setMessage("Data Telah Terdaftar")
                        builder.setPositiveButton("OK", null)
                        builder.show()
                        dismiss()
                    }
                    else{

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> // method to handle errors.
                Toast.makeText(
                    context!!,
                    "Fail to get response = $error",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            override fun getParams(): Map<String, String> {
                // below line we are creating a map for
                // storing our values in key and value pair.
                val params: MutableMap<String, String> =
                    HashMap()

                // on below line we are passing our key
                // and value pair to our parameters.
                params["id_pedagang"] = idpedagang
                params["id_produk"] = idproduk
                params["price"] = price
                params["jumlah"] = jumlah
                params["unit"] = unit
                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        queue.add(request)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        arrayjenis = arrayListOf()
        super.onActivityCreated(savedInstanceState)
        arrayproduk = arrayListOf()
        arrayunit = arrayOf("PCS", "Kg", "Ikat")
        getjenisproduk()
        getproduk()
        var jenis_spinner : Spinner = spinner_jenis
        jenis_spinner.adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, arrayjenis)


        jenis_spinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                if(arrayjenis != null) {
                    val text: String = parent?.getItemAtPosition(position).toString()
                    val key = hasjenis.get(text)
                    Log.d("KEY", key.toString())
                    getprodukbyid(key.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }



        // Initializing an ArrayAdapter
        val spinner: Spinner = spinner_judul_produk
        spinner.adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, arrayproduk)
        // Set an on item selected listener for spinner object

        val spinner2: Spinner = spinner_unit
        spinner2.adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, arrayunit)
        // Set an on item selected listener for spinner object



        btn_tambah_produk.setOnClickListener {
            val idproduk = hasproduk.get(spinner_judul_produk.selectedItem.toString())
            val idpedagang = MainActivity.id_user_pedagang
            val price = et_harga_dialog.text.toString()
            val jumlah = et_jumlah_dialog.text.toString()
            val unit  = spinner_unit.selectedItem.toString()
            if (idpedagang != null) {
                tambahprodukpedagang(idpedagang,idproduk.toString(),price,jumlah,unit)
            }

        }

        btn_tambah_barang.setOnClickListener {
            dismiss()
            val intent = Intent (getActivity(), tambah_produk::class.java)
            getActivity()!!.startActivity(intent)
        }


    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


}