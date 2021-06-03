package com.example.projekskripsi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class home_pedagang_keliling : AppCompatActivity() {
    var idnav = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pedagang_keliling)
        val bottomnav: BottomNavigationView = findViewById(R.id.bottom_navigation_pedagang_keliling)
        bottomnav.setOnNavigationItemSelectedListener(mOnNavigationViewItemSelectedListener)
        val mFragmentManager = supportFragmentManager
        val mfhome = fragment_home_pedagang()
        val fragment = mFragmentManager.findFragmentByTag(fragment_home_pedagang::class.java.simpleName)

        if (fragment !is fragment_home){
            Log.d("home" , "Fragment Name : " + fragment_home_pedagang::class.java.simpleName)
            mFragmentManager
                .beginTransaction()
                .add(R.id.frameContainer2, mfhome,fragment_home_pedagang::class.java.simpleName)
                .commit()
        }

    }

    //function untuk click navigation
    private val mOnNavigationViewItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
            item ->

        when (item.itemId){
            R.id.nav_home ->{
                if(idnav ==1){

                }
                else {
                    val homefragment = fragment_home_pedagang.newInstance()
                    openFragment(homefragment)
                    idnav = 1
                    return@OnNavigationItemSelectedListener true
                }
            }
            R.id.nav_search->{
                if(idnav==2){

                }
                else{
                    val activityfragment = fragment_activity_pedagang.newInstance()
                    openFragment(activityfragment)
                    idnav =2
                    return@OnNavigationItemSelectedListener true
                }

            }
            R.id.nav_person->{
                if(idnav ==3){

                }
                else{
                    return@OnNavigationItemSelectedListener true
                }

            }

        }
        false
    }
    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameContainer2, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}