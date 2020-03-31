package com.example.easyhaircut

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.easyhaircut.classes.User
import com.example.easyhaircut.fragments.HomeFragment
import com.example.easyhaircut.fragments.MapFragment
import com.example.easyhaircut.fragments.ProfileFragment
import com.example.easyhaircut.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InicialActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //Declare Firebase Auth
    private lateinit var db: FirebaseFirestore //Declare Firebase FireStore

    private lateinit var actualUser: User

    private val manager: FragmentManager by lazy { this.supportFragmentManager }
    private val homeFragment: HomeFragment by lazy { HomeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)
        //Declare Bottom Navigation Bar
        var bottomNav:BottomNavigationView=findViewById(R.id.BottomNavigationBar)
        bottomNav.setOnNavigationItemSelectedListener(navListener())
        //Set homeFragment on screen
        var transaction1: FragmentTransaction = manager.beginTransaction()
        transaction1.replace(R.id.FragmentContainer, homeFragment, "homeFragment")
        transaction1.addToBackStack("HomeFragment")
        transaction1.commit()

        auth= FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            //TODO que hacer al tener los permisos
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            }else{
                Toast.makeText(this, getString(R.string.location_denied), Toast.LENGTH_LONG).show()

            }
        }

    }

    /**
     * Listener for bottom navigation bar buttons
     */
    private fun navListener():BottomNavigationView.OnNavigationItemSelectedListener=BottomNavigationView.OnNavigationItemSelectedListener{menuItem ->
        var selectedFragment: Fragment=HomeFragment()
        when (menuItem.itemId) {
            R.id.nav_home -> {
                selectedFragment=HomeFragment()
            }
            R.id.nav_map -> {
                selectedFragment=MapFragment()
            }
            R.id.nav_profile -> {
                selectedFragment=ProfileFragment()
            }
            R.id.nav_settings-> {
                selectedFragment=SettingsFragment()
            }
        }
        val transaction=manager.beginTransaction()
        transaction.replace(R.id.FragmentContainer, selectedFragment)
        transaction.commit()
        return@OnNavigationItemSelectedListener true
    }

}
