package com.example.projekskripsi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_setting_pedagang_keliling.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [setting_pedagang_keliling.newInstance] factory method to
 * create an instance of this fragment.
 */
class setting_pedagang_keliling : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val sharedPrefFile = "kotlinsharedpreference"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_logout.setOnClickListener {
            val preferences = this.activity!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
            preferences.edit().remove("id_user_pedagang").commit()
            val next = Intent(context, MainActivity::class.java)
            startActivity(next)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_pedagang_keliling, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment setting_pedagang_keliling.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            setting_pedagang_keliling().apply {
                arguments = Bundle().apply {

                }
            }
    }
}