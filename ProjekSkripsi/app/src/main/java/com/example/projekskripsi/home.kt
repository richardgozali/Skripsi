package com.example.projekskripsi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
var idnav =0
class home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var idnav =1

       val bottomnav:BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomnav.setOnNavigationItemSelectedListener(mOnNavigationViewItemSelectedListener)
        val mFragmentManager = supportFragmentManager
        val mfhome = fragment_home()
        val fragment = mFragmentManager.findFragmentByTag(fragment_home::class.java.simpleName)

        if (fragment !is fragment_home){
            Log.d("home" , "Fragment Name : " + fragment_home::class.java.simpleName)
            mFragmentManager
                .beginTransaction()
                .add(R.id.frameContainer, mfhome,fragment_home::class.java.simpleName)
                .commit()
        }

    }

    //function untuk click navigation
    private val mOnNavigationViewItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
            item ->

            when (item.itemId){
            R.id.nav_home ->{
                if(idnav ==1){
                    return@OnNavigationItemSelectedListener false
                }
                else {
                    val homefragment = fragment_home.newInstance()
                    idnav = 1
                    openFragment(homefragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            R.id.nav_search->{
                if(idnav ==2){
                    return@OnNavigationItemSelectedListener false
                }
                else {
                    val search_fragment = fragment_activity.newInstance()
                    openFragment(search_fragment)
                    idnav = 2
                    return@OnNavigationItemSelectedListener true
                }
            }
            R.id.nav_person->{

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_book->{
                if(idnav ==3){
                    return@OnNavigationItemSelectedListener false
                }
                else {
                    val menu_fragment: fragment_menu = fragment_menu.newInstance()
                    openFragment(menu_fragment)
                    idnav = 3
                    return@OnNavigationItemSelectedListener true
                }
            }
        }
        false
    }
    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}