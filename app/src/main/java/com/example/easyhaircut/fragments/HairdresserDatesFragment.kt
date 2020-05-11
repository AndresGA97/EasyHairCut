package com.example.easyhaircut.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.easyhaircut.R
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
    lateinit var map:GoogleMap
    private var latitude:Double= 0.0
    private var longitude:Double=0.0

    private lateinit var name: TextView

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var hairdresserRef: CollectionReference =db.collection("hairdresser")

    private var hairdresserName:String=""

    lateinit var calendarView:CalendarView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflateView=inflater.inflate(R.layout.fragment_hairdresser_dates, container, false)

        return inflateView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        name= inflateView!!.findViewById(R.id.textViewHairdresserName)
        calendarView=inflateView!!.findViewById(R.id.calendarViewDates)

        loadHairdresserName()
        loadHairdressers(context!!)
        name.setText(hairdresserName)

        //Declare map fragment
        var mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.mapDates) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //CalendarView
        calendarView.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            Toast.makeText(context, dayOfMonth.toString()+"/"+month+"/"+year, Toast.LENGTH_SHORT).show()
        }

        )
    }

    override fun onMapReady(p0: GoogleMap?) {
        map= p0!!
        var actualPosition: LatLng = LatLng(latitude,longitude)
        map.addMarker(MarkerOptions().position(actualPosition).title(hairdresserName))
        map.moveCamera(CameraUpdateFactory.newLatLng(actualPosition))
    }

    private fun loadHairdressers(context: Context){
        hairdresserRef.whereEqualTo("name", hairdresserName)
            .get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    var actualHairdresser:Hairdresser=documentSnapshot.toObject(Hairdresser::class.java)
                    Toast.makeText(context, actualHairdresser.email, Toast.LENGTH_SHORT).show()
                    latitude=actualHairdresser.address["latitude"]as Double
                    longitude=actualHairdresser.address["longitude"]as Double
                }
            }).addOnFailureListener(OnFailureListener { e ->
                e.message
            })
    }
    private fun loadHairdresserName(){
        var preferences: SharedPreferences =context!!.getSharedPreferences("actualHairdresserClicked", Context.MODE_PRIVATE)
        hairdresserName=preferences.getString("name", "")!!

    }

}
