package com.example.projekskripsi

import android.content.Context
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
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class fragment_activity : Fragment() {
    private lateinit var  adapter: adapateraktivitas

    private  lateinit var  dtnama: ArrayList<String>
    private  lateinit var  dtfoto : TypedArray
    private  lateinit var  dttanggal: ArrayList<String>
    private  lateinit var  dtidorder : ArrayList<Int>
    private  lateinit var  dtstatus : ArrayList<Int>



    private  var armenu = arrayListOf<aktivitas_pedagang_keliling>()
    private var param1: String? = null
    private var param2: String? = null
    private fun getorder(id_pelanggan : String){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getallorder.php?id_pelanggan=" + id_pelanggan
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
                            dtstatus.add(status.toString().toInt())
                            dtidorder.add(id_order.toString().toInt())
                            dttanggal.add(tanggal.toString())
                        }
                            for (position in dtnama.indices){
                                val data = aktivitas_pedagang_keliling(
                                    dtidorder[position],
                                    dtnama[position],
                                    dttanggal[position],
                                    dtstatus[position]
                                )
                                armenu.add(data)
                                Log.v("rigocheck", armenu.toString())
                            }
                            rv_aktivitas.layoutManager  = LinearLayoutManager(context)
                            adapter = adapateraktivitas(armenu,context!!)
                            rv_aktivitas.adapter = adapter
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
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        getorder(MainActivity.id_user_pelanggan.toString())
        dtnama = arrayListOf()
        dtidorder= arrayListOf()
        dttanggal = arrayListOf()
        dtstatus = arrayListOf()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            fragment_activity().apply {
                arguments = Bundle().apply {

                }
            }
    }
}