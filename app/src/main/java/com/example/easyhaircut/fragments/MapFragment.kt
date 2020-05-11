package com.example.easyhaircut.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.easyhaircut.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


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

        //actual position
        loadCoordinate()
        Log.i("latitude", latitude.toString())
        Log.i("longitude", longitude.toString())

        //Declare map fragment
        var mapFragment:SupportMapFragment= childFragmentManager.findFragmentById(R.id.mapDates) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

    private fun loadCoordinate(){
        var preferences: SharedPreferences =context!!.getSharedPreferences("coordinates", Context.MODE_PRIVATE)
        latitude=preferences.getString("latitude", "0.0")!!.toDouble()
        longitude=preferences.getString("longitude", "0.0")!!.toDouble()

    }
}
