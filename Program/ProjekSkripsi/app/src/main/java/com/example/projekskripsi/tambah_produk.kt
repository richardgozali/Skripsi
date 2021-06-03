package com.example.projekskripsi

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.t2r2.volleyexample.FileDataPart
import com.t2r2.volleyexample.VolleyFileUploadRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_tambah_produk.*
import kotlinx.android.synthetic.main.custom_dialog.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class tambah_produk : AppCompatActivity() {

        private  lateinit var  arrayunit: Array<String>
        lateinit var imageView: ImageView
        lateinit var button: Button
        private val pickImage = 100
        private var imageUri: Uri? = null
        private val mListener: Response.Listener<NetworkResponse>? = null
        private val mErrorListener: Response.ErrorListener? = null
        private  lateinit var  arrayjenis : ArrayList<String>
        private var hasjenis: HashMap<String,Int > = HashMap<String,Int>()


    private  fun uploadimage(imageString:String,namaproduk : String, jenisproduk: String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/insertproduk.php"
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
                    Log.d("hasil", respObj.toString())
                    ll_tambah_produk.visibility = View.VISIBLE
                    pb_tambah_barang.visibility = View.GONE

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
                params["image"] = imageString
                params["nama_produk"] = namaproduk
                params["id_jenis"] = jenisproduk
                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        queue.add(request)
    }
    private  fun getjenisproduk(){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getjenis.php"

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(this)
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
                            val id_jenis = jenis.get("id_jenis")
                            val keterangan = jenis.get("keterangan")
                            arrayjenis.add(nama_jenis.toString())
                            hasjenis.put(nama_jenis.toString(),i+1)
                            Log.d("jenis" , id_jenis.toString())
                        }
                        Log.d("array jenis" , arrayjenis.toString())
                        var jenis_spinner : Spinner = spinner_jenis_tambah_produk
                        jenis_spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayjenis)

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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            imageView.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_tambah_produk)
            imageView = findViewById(R.id.imageView)
            button = findViewById(R.id.buttonLoadPicture)
            button.setOnClickListener {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)

            }
            getjenisproduk()
            save_produk.setOnClickListener {
                var nama_produk = et_nama_produk.text.toString()
                var jenis_produk = spinner_jenis_tambah_produk.selectedItem.toString()
                if(imageView.getDrawable()==null){

                }
                else{


                    if(nama_produk.toString() == "" && jenis_produk != ""){

                    }
                    else{
                        ll_tambah_produk.visibility = View.GONE
                        pb_tambah_barang.visibility = View.VISIBLE
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        val bitmap = (imageView.getDrawable() as BitmapDrawable).bitmap

                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
                        var imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
                        val imageString: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                        val key = hasjenis.get(spinner_jenis_tambah_produk.selectedItem.toString())
                        uploadimage(imageString,nama_produk,key.toString())
                        Log.d("masuk" , "coba")
                    }

                }
            }
            arrayunit = arrayOf("PCS", "Kg", "Ikat")
            val spinner2: Spinner = spinner_jenis_tambah_produk
            spinner2.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayunit)
        }

}