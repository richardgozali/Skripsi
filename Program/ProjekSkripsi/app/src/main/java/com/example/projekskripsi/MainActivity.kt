package com.example.projekskripsi

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.dropdown_spinner
import kotlinx.android.synthetic.main.activity_registrasi.*
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() ,AdapterView.OnItemClickListener{
    companion object{
        var id_user_pelanggan : String? = null
        var id_user_pedagang : String? = null
    }

    var dropdown  = arrayOf("Pedagang Keliling","Pelanggan")

    var spinner: Spinner? = null

    var checking : String? = null

    private var locationManager : LocationManager? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //sharedprefrences menyimpan
        var sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getInt("id_user_pelanggan",0)
        val iduserpedagang =  sharedPreferences.getInt("id_user_pedagang",0)
        if(sharedIdValue.equals(0)){
            Log.e("tdkMasuk",sharedIdValue.toString())

        }else{
            Log.e("Masuk",sharedIdValue.toString())
            id_user_pelanggan = sharedIdValue.toString()
            val next = Intent(applicationContext, home::class.java)
            startActivity(next)
            pb_login.visibility = View.GONE
        }
        if (iduserpedagang != 0){
            pb_login.visibility = View.GONE
            id_user_pedagang = iduserpedagang.toString()
            val next = Intent(applicationContext, home_pedagang_keliling::class.java)
            startActivity(next)
        }


        val spinner: Spinner = findViewById(R.id.dropdown_spinner)

        ArrayAdapter.createFromResource(
            this,
            R.array.home,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        registrasi_btn.setOnClickListener {
            val next = Intent(applicationContext, Registrasi::class.java)
            startActivity(next)
        }
        btn_signin.setOnClickListener {
            if(dropdown_spinner.selectedItem.toString()=="Pelanggan"){
                signin("+62"+et_telepon_login.text.toString(), et_password_login.text.toString());

            }
            else{
                signinpedagang("+62"+et_telepon_login.text.toString(), et_password_login.text.toString())
            }

        }
        if (ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)


            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }

        }
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }
    private fun signin(telepon: String, password: String) {
        // url to post our data
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getpelanggan.php"
        // creating a new variable for our request queue
        pb_login.visibility = View.VISIBLE
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
                et_telepon_login.setText("");
                et_password_login.setText("");

                // on below line we are displaying a success toast message.

                try {
                    // on below line we are passing our response
                    // to json object to extract data from it.
                    val respObj = JSONObject(response)

                    // below are the strings which we
                    // extract from our json object.

                    var msg = respObj.get("msg")
                    Log.d("msg", msg.toString())
                    if(msg.toString() == "error"){
                        val builder = AlertDialog.Builder(this, R.style.Alertlogin)
                        builder.setTitle("LOGIN GAGAL")
                        builder.setMessage("PASSWORD ATAU TELEPON TIDAK VALID")
                        builder.setPositiveButton("OK", null)

                        builder.show()
                        pb_login.visibility = View.GONE
                    }
                    else{

                        id_user_pelanggan = respObj.get("id_pelanggan").toString()

                        //sharedprefrences
                        var sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                        editor.putInt("id_user_pelanggan", id_user_pelanggan.toString().toInt())
                        editor.apply()
                        editor.commit()

                        val next = Intent(applicationContext, home::class.java)
                        startActivity(next)
                        pb_login.visibility = View.GONE
                    }

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
                params["telepon"] = telepon
                params["password"] = password

                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        queue.add(request)
    }

    private fun signinpedagang(telepon: String, password: String) {
        // url to post our data
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getpedagang.php"
        pb_login.visibility = View.VISIBLE
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
                et_telepon_login.setText("");
                et_password_login.setText("");

                // on below line we are displaying a success toast message.

                try {
                    // on below line we are passing our response
                    // to json object to extract data from it.
                    val respObj = JSONObject(response)

                    // below are the strings which we
                    // extract from our json object.

                    var msg = respObj.get("msg")

                    if(msg.toString() == "error"){
                        val builder = AlertDialog.Builder(this, R.style.Alertlogin)
                        builder.setTitle("LOGIN GAGAL")
                        builder.setMessage("PASSWORD ATAU TELEPON TIDAK VALID")
                        builder.setPositiveButton("OK", null)
                        builder.show()
                        pb_login.visibility = View.GONE
                    }
                    else{
                        id_user_pedagang = respObj.get("id_pedagang").toString()



                        var sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

                        val editors = sharedPreferences.edit()
                        editors.clear()
                        editors.apply()

                        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                        editor.putInt("id_user_pedagang", id_user_pedagang.toString().toInt())
                        editor.apply()
                        editor.commit()


                        pb_login.visibility = View.GONE
                        val next = Intent(applicationContext, home_pedagang_keliling::class.java)
                        startActivity(next)
                    }

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
                params["telepon"] = telepon
                params["password"] = password

                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        queue.add(request)
    }
}