package com.example.easyhaircut.fragments

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.example.easyhaircut.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import java.io.InputStream
import java.io.InputStreamReader


/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment(), OnMapReadyCallback {
    private var inflateView:View?=null
    lateinit var map:GoogleMap
    private var latitude:Double= 0.0
    private var longitude:Double=0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflateView = inflater.inflate(R.layout.fragment_map, container, false)
        //Declare map fragment
        var mapFragment:SupportMapFragment= childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //actual position
        loadCoordinate()
        Log.i("latitude", latitude.toString())
        Log.i("longitude", longitude.toString())

        //TODO reading google api place


        return inflateView
    }

    /**
     * Google maps function
     */
    override fun onMapReady(p0: GoogleMap?) {
        map= p0!!
        var actualPosition:LatLng= LatLng(latitude,longitude)
        map.addMarker(MarkerOptions().position(actualPosition).title("Your position"))
        map.moveCamera(CameraUpdateFactory.newLatLng(actualPosition))
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.getDouble("latitude")?.let { latitude=it }
        arguments?.getDouble("longitude")?.let { longitude=it }
    }
    private fun loadCoordinate(){
        var preferences: SharedPreferences =context!!.getSharedPreferences("coordinates", Context.MODE_PRIVATE)
        latitude=preferences.getString("latitude", "0.0")!!.toDouble()
        longitude=preferences.getString("longitude", "0.0")!!.toDouble()

    }
}
