package com.example.projekskripsi

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_main.dropdown_spinner
import kotlinx.android.synthetic.main.activity_registrasi.*
import org.json.JSONException
import org.json.JSONObject


class Registrasi : AppCompatActivity() {
    companion object{
        var lokasi : LatLng? =  null
        var tiperegis: String = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        // Create a new PlacesClient instance
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyCZUq3a4q1Unb6y0tYL6UXBZZfEyuwbbtM")
            Log.d("Maps" , "Maps Activated")
        }
        else{
            Log.d("error" , "Maps gagal")
        }
        // Initialize the SDK

        val placesClient = Places.createClient(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i("Place:" ,"${place.name}, ${place.id}, ${place.latLng}")

                val text = "Hello toast!"
                val duration = Toast.LENGTH_SHORT
                lokasi = place.latLng
                val toast = Toast.makeText(applicationContext, place.latLng.toString(), duration)
                toast.show()

            }

            override fun onError(status: Status) {
                Log.i("error", "An error occurred: $status")
            }

        })

        // Spinner for dropdown
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
        dropdown_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val text = parent?.getItemAtPosition(position).toString()
                tiperegis = text
                if(text == "Pedagang Keliling"){
                    autocomplete_fragment.getView()?.setVisibility(View.VISIBLE);
                }
                else{
                    autocomplete_fragment.getView()?.setVisibility(View.GONE);
                }
            }

        }

        //  buat fungsi untuk pembuatan daftar API.
        btn_daftar.setOnClickListener {
            if(tiperegis !="Pedagang Keliling" ){
                if(et_nomer_telepone.text.toString() != "" && et_password.text.toString() != "" && et_nama.text.toString() != "" ){
                    postDatapelanggan("+62"+et_nomer_telepone.getText().toString(), et_password.getText().toString(),et_nama.text.toString());
                }
                else{
                    val builder = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                    builder.setTitle("PERINGATAN")
                    builder.setMessage("DATA HARAP DIISI")
                    builder.setPositiveButton("OK", null)

                    builder.show()
                }

            }
            else{
                if(lokasi != null  && et_nomer_telepone.text.toString() != "" && et_password.text.toString() != "" && et_nama.text.toString() != ""){
                    postDatapedagang("+62"+et_nomer_telepone.getText().toString(), et_password.getText().toString(),et_nama.text.toString(),lokasi!!)
                }
                else{
                    val builder = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                    builder.setTitle("PERINGATAN")
                    builder.setMessage("DATA HARAP DIISI")
                    builder.setPositiveButton("OK", null)
                    builder.show()
                }
            }

        }

    }
    private fun postDatapedagang(telepon: String, password: String,nama_lengkap : String, lokasi : LatLng) {

        // url to post our data
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/insertpedagangkeliling.php"


        //loadingPB.setVisibility(View.VISIBLE)

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
                et_nomer_telepone.setText("")
                et_password.setText("")
                et_nama.setText("")

                // on below line we are displaying a success toast message.
                Toast.makeText(this, "Data Telah Terdaftar", Toast.LENGTH_SHORT)
                    .show()
                try {
                    // on below line we are passing our response
                    // to json object to extract data from it.
                    val respObj = JSONObject(response)

                    // below are the strings which we
                    // extract from our json object.
                    Log.e("response", respObj.toString())
                    val next = Intent(applicationContext, MainActivity::class.java)
                    startActivity(next)

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
                params["nama_lengkap"] = nama_lengkap
                params["region_longitude"] = lokasi.longitude.toString()
                params["region_latitude"] = lokasi.latitude.toString()
                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        queue.add(request)
    }

    private fun postDatapelanggan(telepon: String, password: String,nama_lengkap : String) {
        // url to post our data
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/insertpelanggan.php"

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
                    et_nomer_telepone.setText("")
                    et_password.setText("")
                    et_nama.setText("")

                    // on below line we are displaying a success toast message.
                    Toast.makeText(this, "Data Telah Terdaftar", Toast.LENGTH_SHORT)
                        .show()
                    try {
                        // on below line we are passing our response
                        // to json object to extract data from it.
                        val respObj = JSONObject(response)

                        // below are the strings which we
                        // extract from our json object.
                        Log.e("response", respObj.toString())
                        val next = Intent(applicationContext, MainActivity::class.java)
                        startActivity(next)

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
                    params["nama_lengkap"] = nama_lengkap

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