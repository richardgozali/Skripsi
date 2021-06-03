package com.example.projekskripsi

import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_activity_pedagang.*
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



class fragment_activity_pedagang : Fragment() {

    private lateinit var  adapter: adapaterpelanggan

    private  lateinit var  dtnama: ArrayList<String>
    private  lateinit var  dtidpelanggan :ArrayList<Int>
    private  lateinit var  dtidorder : ArrayList<Int>
    private  lateinit var  dtstatus: ArrayList<Int>
    private  var armenu = arrayListOf<pelanggan>()

    private  fun getorderbypedagagn(){
        val url ="https://tos.petra.ac.id/~c14170049/Skripsi/getorderbyidpedagang.php?id_pedagang="+ MainActivity.id_user_pedagang.toString()
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
                            val order = respObj.getJSONObject(i.toString())
                            val id_order = order.get("id_order")
                            val tanggal = order.get("tanggal")
                            val status = order.get("status")
                            val nama_lengap = order.get("nama_lengkap")
                            dtnama.add(nama_lengap.toString())
                            dtidorder.add(id_order.toString().toInt())
                            dtstatus.add(status.toString().toInt())
                            dtidpelanggan.add(order.get("id_pelanggan").toString().toInt())
                        }
                        for (position in dtnama.indices){
                            val data = pelanggan(
                                dtidorder[position],
                                dtidpelanggan[position],
                                dtnama[position],
                                dtstatus[position]
                            )
                            armenu.add(data)

                        }
                        rv_aktivitas_pedagang.layoutManager  = LinearLayoutManager(context)
                        adapter = adapaterpelanggan(armenu,context!!)
                        rv_aktivitas_pedagang.adapter = adapter
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dtnama = arrayListOf()
        dtstatus = arrayListOf()
        dtidpelanggan = arrayListOf()
        dtidorder = arrayListOf()
        getorderbypedagagn()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_pedagang, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_activity_pedagang.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            fragment_activity_pedagang().apply {

            }
    }
}