package com.example.projekskripsi

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class chat : AppCompatActivity() {

    private lateinit var  adapter: adapterchat
    private  lateinit var  dtnama_pelanggan: ArrayList<String>
    private  lateinit var  dtnama_pedagang :  ArrayList<String>
    private  lateinit var  dt_tipe : ArrayList<Int>
    private  lateinit var  dt_pesan :  ArrayList<String>
    var idpengirim = ""
    var idpenerima = ""
    var idtoken = ""
    var idtipe = ""
    private  var armenu = arrayListOf<chats>()
    var nama = ""
    private fun getmessage(id_chat_room : String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getmessage.php?id_chat_room="+ id_chat_room
        val queue = Volley.newRequestQueue(this)
        Log.e("idchatroom", url)
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

                    if(msg.toString() == "error") {

                    }
                    else if (msg.toString() == "success") {
                        for (i in 0 until respObj.length() - 1) {
                            val message = respObj.getJSONObject(i.toString())
                            val pesan = message.get("message")
                            val id_pengirim = message.get("id_pengirim")
                            val id_penerima = message.get("id_penerima")
                            if(id_pengirim == idpengirim){
                                dt_tipe.add(1)
                            }
                            else{
                                dt_tipe.add(2)
                            }
                            dt_pesan.add(pesan.toString())
                        }
                        for (position in dt_pesan.indices){
                            val data = chats(
                                dt_tipe[position],
                                dt_pesan[position]
                            )

                            armenu.add(data)
                            Log.v("rigocheck", armenu.toString())
                        }
                        rv_chat.layoutManager  = LinearLayoutManager(this)
                        adapter = adapterchat(armenu)
                        rv_chat.adapter = adapter
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
    private fun sendmessage(id_chat_room: String , pesan :String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/sendmessage.php"
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
                et_message_chat.setText("")
                // on below line we are displaying a success toast message.

                try {
                    // on below line we are passing our response
                    // to json object to extract data from it.
                    val respObj = JSONObject(response)

                    // below are the strings which we
                    // extract from our json object.

                    dtnama_pelanggan = arrayListOf()
                    dtnama_pedagang = arrayListOf()
                    dt_tipe = arrayListOf()
                    dt_pesan = arrayListOf()
                    armenu= arrayListOf()
                    getmessage(id_chat_room)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> // method to handle errors.
                Toast.makeText(
                    this,
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
                params["id_chat_room"] = id_chat_room
                params["id_pengirim"] = idpengirim
                params["id_penerima"] = idpenerima
                params["message"] = pesan
                params["id_token"] = idtoken
                params["tipe"] = idtipe
                params["nama"] = tv_nama_pedagang_chat.text.toString()
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
        setContentView(R.layout.activity_chat)
        val id_order=intent.getStringExtra("id_order")


        val id_chat_room=intent.getIntExtra("idchatroom",0)
        val tipe = intent.getIntExtra("tipe",0)

        if(tipe == 1){
            val nama = intent.getStringExtra("namapedagang")
            tv_nama_pedagang_chat.setText(nama.toString())
            val id_pelanggan = intent.getIntExtra("idpelanggan",0)
            idpengirim="pl_"+id_pelanggan.toString()
            val id_pedagang = intent.getIntExtra("idpedagang",0)
            idpenerima = "pd_"+ id_pedagang.toString()
            idtoken = id_pedagang.toString()
            idtipe = 1.toString()
        }
        else{
            val nama = intent.getStringExtra("namapelanggan")
            tv_nama_pedagang_chat.setText(nama.toString())
            val id_pelanggan = intent.getIntExtra("idpelanggan",0)

            Log.e("idpengirim", idpengirim.toString())
            val id_pedagang = intent.getIntExtra("idpedagang",0)
            idpenerima = "pl_"+ id_pelanggan.toString()
            idtoken = id_pelanggan.toString()
            idpengirim="pd_"+id_pedagang.toString()
            idtipe= 2.toString()
        }


        getmessage(id_chat_room.toString())

        dtnama_pelanggan = arrayListOf()
        dtnama_pedagang = arrayListOf()
        dt_tipe = arrayListOf()
        dt_pesan = arrayListOf()
        iv_back.setOnClickListener {
            onBackPressed()
        }
        btn_sendmessage.setOnClickListener {
            sendmessage(id_chat_room.toString(), et_message_chat.text.toString())
        }

        et_message_chat.setOnFocusChangeListener { v, hasFocus ->
            fun Activity.hideKeyboard(view: View) {
                hideKeyboard(currentFocus ?: View(this))
            }
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
    }
}