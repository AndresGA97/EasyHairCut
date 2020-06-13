package com.example.easyhaircut.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.easyhaircut.R
import com.example.easyhaircut.alerts.AlertAddDate
import com.example.easyhaircut.classes.Dates
import com.example.easyhaircut.classes.Hairdresser
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

/**
 * A simple [Fragment] subclass.
 */
class HairdresserDatesFragment : Fragment(), OnMapReadyCallback {
    private var inflateView:View?=null
    private var transaction1: FragmentTransaction?=null
    private val fragmentSettingsDatesFragment:SettingsDatesFragment by lazy { SettingsDatesFragment() }

    lateinit var map:GoogleMap
    private var latitude:Double= 0.0
    private var longitude:Double=0.0
    private var userType:Boolean=false

    private var min=0; var max=0; var interval=0; var rest=0

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var hairdresserRef: CollectionReference =db.collection("hairdresser")

    private var hairdresserName:String=""
    private var hairdresserEmail=""

    lateinit var calendarView:CalendarView
    lateinit var changeScheduleButton: Button
    private lateinit var name: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadHairdresserName()
        loadHairdressers()
        loadUserType()
        loadDateSettings()

        // Inflate the layout for this fragment
        inflateView=inflater.inflate(R.layout.fragment_hairdresser_dates, container, false)

        //Declare map fragment
        var mapFragment:SupportMapFragment= childFragmentManager.findFragmentById(R.id.mapDates) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return inflateView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        name= inflateView!!.findViewById(R.id.textViewHairdresserName)
        calendarView=inflateView!!.findViewById(R.id.calendarViewDates)
        changeScheduleButton=inflateView!!.findViewById(R.id.buttonChangeSchedule)


        if(userType){
            changeScheduleButton.visibility=View.VISIBLE
        }
        name.setText(hairdresserName)


        changeScheduleButton.setOnClickListener { v ->
            var fragmentManager: FragmentManager? = fragmentManager
            if (fragmentManager != null) {
                transaction1=fragmentManager.beginTransaction()
            }
            transaction1!!.replace(R.id.FragmentContainer,fragmentSettingsDatesFragment,"fragmentSettingsDates")
            transaction1!!.addToBackStack("fragmentSettingsDates")
            transaction1!!.commit()
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        map= p0!!
        var actualPosition: LatLng = LatLng(latitude,longitude)
        map.addMarker(MarkerOptions().position(actualPosition).title(hairdresserName))
        map.moveCamera(CameraUpdateFactory.newLatLng(actualPosition))
    }

    private fun loadHairdressers(){
        hairdresserRef.whereEqualTo("name", hairdresserName)
            .get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    var name=documentSnapshot.get("name") as String
                    var address=documentSnapshot.get("address") as HashMap<String, Double>
                    var latLng= LatLng(address.get("latitude")!!, address.get("longitude")!!)
                    var email=documentSnapshot.get("email") as String
                    var password=documentSnapshot.get("password") as String
                    var dates= ArrayList<Dates>()
                    var actualHairdresser:Hairdresser= Hairdresser(name, latLng, email, password, dates)
                    hairdresserEmail=actualHairdresser.email
                    latitude=actualHairdresser.address["latitude"]as Double
                    longitude=actualHairdresser.address["longitude"]as Double

                    //CalendarView
                    calendarView.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
                        var fragmentManager: FragmentManager? = fragmentManager
                        var alert: AlertAddDate =AlertAddDate(this.activity!!, year, month+1, dayOfMonth, hairdresserEmail, min, max, interval, rest)
                        alert.show(fragmentManager!!,"alert")
                    })
                }
            }).addOnFailureListener(OnFailureListener { e ->
                e.message
            })
    }
    private fun loadHairdresserName(){
        var preferences: SharedPreferences =context!!.getSharedPreferences("actualHairdresserClicked", Context.MODE_PRIVATE)
        hairdresserName=preferences.getString("name", "")!!
    }
    private fun loadUserType(){
        var preferences=context!!.getSharedPreferences("userType", Context.MODE_PRIVATE)
        userType=preferences.getBoolean("user", false)
    }
    private fun loadDateSettings(){
        var preferences=context!!.getSharedPreferences("dateSetting", Context.MODE_PRIVATE)
        min=preferences.getInt("min", 9)
        max=preferences.getInt("max", 21)
        interval=preferences.getInt("interval", 60)
        rest=preferences.getInt("rest", 0)
    }

}
