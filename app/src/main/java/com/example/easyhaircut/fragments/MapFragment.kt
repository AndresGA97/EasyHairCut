package com.example.easyhaircut.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.easyhaircut.R
import com.example.easyhaircut.classes.Dates
import com.example.easyhaircut.classes.Hairdresser
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    private var inflateView:View?=null
    lateinit var map:GoogleMap
    private var latitude:Double= 0.0
    private var longitude:Double=0.0

    private var transaction: FragmentTransaction?=null
    private val fragmentDates:HairdresserDatesFragment by lazy { HairdresserDatesFragment() }

    lateinit var hairdresserList:ArrayList<Hairdresser>
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var hairdresserRef: CollectionReference =db.collection("hairdresser")

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //loadHairdresser
        hairdresserList=ArrayList()
        loadHairdressers(context!!)
    }

    /**
     * Google maps function
     */
    override fun onMapReady(p0: GoogleMap?) {
        map= p0!!
        var actualPosition:LatLng= LatLng(latitude,longitude)
        map.addMarker(MarkerOptions().position(actualPosition).title("Your position"))
        map.moveCamera(CameraUpdateFactory.newLatLng(actualPosition))
        map.setOnMarkerClickListener(this)
        map.setOnInfoWindowClickListener(this)
    }

    /**
     * Marker click listener
     */
    override fun onMarkerClick(p0: Marker?): Boolean {
        if (p0!!.isInfoWindowShown) {
            p0.hideInfoWindow()
        } else {
            p0.showInfoWindow()
        }
        return true
    }

    /**
     * InfoWindow Click Listener
     */
    override fun onInfoWindowClick(p0: Marker?) {
        saveHairdresserName(p0!!.title)
        var fragmentManager: FragmentManager? = fragmentManager
        if (fragmentManager != null) {
            transaction=fragmentManager.beginTransaction()
        }
        transaction!!.replace(R.id.FragmentContainer,fragmentDates,"fragmentDates")
        transaction!!.addToBackStack("fragmentDates")
        transaction!!.commit()
    }

    private fun loadCoordinate(){
        var preferences: SharedPreferences =context!!.getSharedPreferences("coordinates", Context.MODE_PRIVATE)
        latitude=preferences.getString("latitude", "0.0")!!.toDouble()
        longitude=preferences.getString("longitude", "0.0")!!.toDouble()

    }

    private fun loadHairdressers(context:Context){
        hairdresserRef.get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                hairdresserList= ArrayList()
                for (documentSnapshot in queryDocumentSnapshots) {
                    var data=documentSnapshot.data
                    var name:String=data["name"] as String
                    var address:HashMap<String, Double> = data["address"] as HashMap<String, Double>
                    var latLng= LatLng(address.get("latitude")!!, address.get("longitude")!!)
                    var email:String=data["email"] as String
                    var password:String=data["password"] as String
                    var dates:ArrayList<Dates> = data["dates"] as ArrayList<Dates>

                    var actualHairdresser=Hairdresser(name, latLng, email, password, dates)
                    hairdresserList.add(actualHairdresser)

                    map.addMarker(MarkerOptions().position(latLng).title(name))
                }
            })
    }

    private fun saveHairdresserName(title:String){
        var preferences: SharedPreferences =activity!!.getSharedPreferences("actualHairdresserClicked", Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor=preferences.edit()
        editor.putString("name", title)
        editor.commit()
    }
}
