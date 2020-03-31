package com.example.easyhaircut.fragments

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflateView = inflater.inflate(R.layout.fragment_map, container, false)
        //Declare map fragment
        var mapFragment:SupportMapFragment= childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return inflateView
    }

    /**
     * Google maps function
     */
    override fun onMapReady(p0: GoogleMap?) {
        map= p0!!
        var alpanseque:LatLng= LatLng(41.276820, -2.681686)
        map.addMarker(MarkerOptions().position(alpanseque).title("Alpanseque"))
        map.moveCamera(CameraUpdateFactory.newLatLng(alpanseque))
    }

}
