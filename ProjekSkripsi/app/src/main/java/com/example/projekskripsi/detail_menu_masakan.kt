package com.example.projekskripsi

import android.R.attr.name
import android.R.id
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_menu_masakan.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class detail_menu_masakan : AppCompatActivity() {
    private fun getmenumakanan(id_masakan : Int){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getmenumakananbyid.php?id_masakan="+id_masakan
        Log.e("URL" , url)
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
                    Log.d("response", respObj.toString())
                    var msg = respObj.get("msg")

                    if(msg.toString() == "success"){
                        for (i in 0 until respObj.length() - 1) {
                            val masakan = respObj.getJSONObject(i.toString())
                            val id_menu_makanan = masakan.get("id_masakan")
                            val nama_masakan = masakan.get("nama_masakan")
                            val img_masakan = masakan.get("img_makanan")
                            val bahan = masakan.get("bahan")
                            val bahans = JSONArray(bahan.toString())
                            var stepstepbahan = "Bahan Makanan : \n \n"

                            for ( j in 0 until bahans.length() -1 ){
                                var bahanbahan = bahans.getString(j)
                               bahanbahan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    Html.fromHtml(bahanbahan, Html.FROM_HTML_MODE_LEGACY).toString()
                                } else {
                                    Html.fromHtml(bahanbahan).toString()
                                }
                                stepstepbahan += "\u2022"+ Html.fromHtml(bahanbahan) + "\n"

                            }

                            val cara_masak = masakan.get("cara_masak")
                            val cara_masaks = JSONArray(cara_masak.toString())
                            var stepmasak =  "Cara Masak : \n \n"

                            for ( j in 0 until cara_masaks.length() ){
                                val step = JSONObject(cara_masaks.getString(j))
                                var caracaramasak = step.get("text").toString()
                                caracaramasak = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    Html.fromHtml(caracaramasak).toString()
                                } else {
                                    Html.fromHtml(caracaramasak).toString()
                                }


                                stepmasak +=  (j+1).toString()+". " + Html.fromHtml(caracaramasak) + "\n"

                            }

                            Picasso.get().load(img_masakan.toString()).into(iv_foto_masakan)
                            tv_nama_masakan.setText(nama_masakan.toString())
                            tv_bahan_masakan.setText((stepstepbahan))
                            tv_cara_memasak.setText((stepmasak))
                            pb_detail_masakan.visibility = View.GONE
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_menu_masakan)
        val id_menu=intent.getIntExtra("id_masakan", 0)
        pb_detail_masakan.visibility = View.VISIBLE
        getmenumakanan(id_menu)
    }
}