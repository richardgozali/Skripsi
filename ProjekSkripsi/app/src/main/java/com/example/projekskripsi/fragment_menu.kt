package com.example.projekskripsi

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home_pedagang.*
import kotlinx.android.synthetic.main.fragment_menu.*
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_menu.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_menu : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var  adapter: adaptermenu_makanan

    private  lateinit var  dtnama: ArrayList<String>
    private  lateinit var  dtidmenu: ArrayList<Int>
    private  lateinit var  dtfoto : ArrayList<String>

    private  var armenu = arrayListOf<menu_makanan>()


    //Mengambil menu makanan melauli API
    private fun getmenumakanan(){
        val url = "https://tos.petra.ac.id/~c14170049/Skripsi/getmenumakanan.php?id_pelanggan="+ MainActivity.id_user_pelanggan
        // creating a new variable for our request queue
        btn_tambah_menu.visibility = View.GONE
        rv_menu.visibility = View.GONE
        pb_fragment_menu.visibility = View.VISIBLE

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
                dtfoto= arrayListOf()
                dtidmenu = arrayListOf()
                dtnama = arrayListOf()
                armenu.clear()
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
                            dtfoto.add(img_masakan.toString())
                            dtnama.add(nama_masakan.toString())
                            dtidmenu.add(id_menu_makanan.toString().toInt())
                        }

                        for (position in dtnama.indices) {
                            val data = menu_makanan(
                                dtidmenu[position],
                                dtfoto[position],
                                dtnama[position]
                            )

                            armenu.add(data)
                        }
                        rv_menu.layoutManager  = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
                        adapter = adaptermenu_makanan(armenu,context!!)
                        rv_menu.adapter = adapter
                        pb_fragment_menu.visibility = View.GONE
                        rv_menu.visibility = View.VISIBLE
                        btn_tambah_menu.visibility = View.VISIBLE
                    }

                    else {
                        for (position in dtnama.indices) {
                            val data = menu_makanan(
                                dtidmenu[position],
                                dtfoto[position],
                                dtnama[position]
                            )

                            armenu.add(data)
                        }
                        rv_menu.layoutManager  = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
                        adapter = adaptermenu_makanan(armenu,context!!)
                        rv_menu.adapter = adapter
                        pb_fragment_menu.visibility = View.GONE
                        rv_menu.visibility = View.VISIBLE
                        btn_tambah_menu.visibility = View.VISIBLE

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
        dtfoto= arrayListOf()
        dtidmenu = arrayListOf()
        dtnama = arrayListOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getmenumakanan()
        btn_tambah_menu.setOnClickListener {
                val fragment = menu_dialog()
                fragment.show(childFragmentManager,"coba")
        }

        refresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context!!, R.color.quantum_white_100))
        refresh.setColorSchemeColors(Color.GREEN)

        refresh.setOnRefreshListener {
            getmenumakanan()
            refresh.isRefreshing = false
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_menu, container, false)

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_menu.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            fragment_menu().apply {
                arguments = Bundle().apply {

                }
            }
    }
}