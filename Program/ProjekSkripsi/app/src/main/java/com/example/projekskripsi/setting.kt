package com.example.projekskripsi

import CustomProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_detail_aktivitas.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setting.*
import org.json.JSONException
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [setting.newInstance] factory method to
 * create an instance of this fragment.
 */
class setting : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var namapelanggan =""
    var telepon = ""
    var passwords = ""
    private val progressDialog = CustomProgressDialog()
    private val sharedPrefFile = "kotlinsharedpreference"
    private fun getpelanggan(){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getpelangganbyid.php?id_pelanggan=" + MainActivity.id_user_pelanggan
        val queue = Volley.newRequestQueue(context)

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
                            namapelanggan = nama.toString()
                            telepon = pedagang.get("telepon").toString()
                            passwords = pedagang.get("password").toString()
                            tv_real_nmr_telepon.setText(telepon)
                            tv_nama_lengkap_real.setText(namapelanggan)
                            tv_password_real.setText(passwords)

                        }

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
    private fun updatepelanggan(telepon:String, idpelanggan :String , namalengkap :String,password:String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/updatedatapelanggan.php"
        val queue = Volley.newRequestQueue(context)

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
                    Log.d("msg", msg.toString())
                    if(msg.toString() == "success"){
                        progressDialog.dialog.setTitle("Success")
                        Log.e("sucee", "Successs")
                    }
                    else{
                        progressDialog.dialog.setTitle("Error")
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        // Dismiss progress bar after 4 seconds
                        progressDialog.dialog.dismiss()
                    }, 2000)
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
                params["telepon"] = telepon
                params ["id_pelanggan"] = idpelanggan
                params["password"] =password
                params["nama_lengkap"] =namalengkap

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
        getpelanggan()
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_save.setOnClickListener {

            if(namapelanggan != tv_nama_lengkap_real.text.toString() || telepon != tv_real_nmr_telepon.text.toString() || passwords != tv_password_real.text.toString()){
                progressDialog.show(context!!,"Please Wait...")
                updatepelanggan(tv_real_nmr_telepon.text.toString(),MainActivity.id_user_pelanggan.toString(),tv_nama_lengkap_real.text.toString(),tv_password_real.text.toString())
            }
            else{

            }
        }
        btn_logout.setOnClickListener {

            val preferences = this.activity!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
            preferences.edit().remove("id_user_pelanggan").commit()
            val next = Intent(context, MainActivity::class.java)
            startActivity(next)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment setting.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            setting().apply {
                arguments = Bundle().apply {

                }
            }
    }
}