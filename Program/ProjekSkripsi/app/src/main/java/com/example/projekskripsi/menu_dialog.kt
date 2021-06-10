package com.example.projekskripsi

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import kotlinx.android.synthetic.main.menu_custom_dialog.*
import kotlinx.android.synthetic.main.menu_custom_dialog.btn_tambah_menu
import org.json.JSONException
import org.json.JSONObject


class menu_dialog: DialogFragment() {
    private val progressDialog = CustomProgressDialog()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
        val view = inflater.inflate(R.layout.menu_custom_dialog, container, false)

        return view

        // Initializing a String Array

    }
    // Tambah Menu Makanan melalui API
    private fun tambahmenumakanan(namamenu:String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/runpython.php?nama_makan="+namamenu+"&idpelanggan=" +  MainActivity.id_user_pelanggan.toString()
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
                    var msg = respObj.get("msg")

                    if(msg.toString() == "success"){
                        progressDialog.dialog.dismiss()

                        val intent = Intent(context, home::class.java)
                        startActivity(intent)
                        home.idnav=2
                    }
                    else{
                        val builder = AlertDialog.Builder(context!!, R.style.Alertlogin)
                        builder.setTitle("PERINGATAN")
                        builder.setMessage("MENU MAKANAN TIDAK ADA")
                        builder.setPositiveButton("OK", null)
                        builder.show()
                    }
                    progressDialog.dialog.dismiss()
                    btn_tambah_menu.isEnabled = true
                    dismiss()
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
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_tambah_menu.setOnClickListener {
            progressDialog.show(context!!,"Please Wait...")

            tambahmenumakanan(et_nama_menu.text.toString().toLowerCase())
            btn_tambah_menu.isEnabled = false
        }
    }
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}