package com.example.easyhaircut.classes

import android.location.Address
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Hairdresser class
 * @author Andrés
 */
class Hairdresser {
    lateinit var name:String
    var address= mutableMapOf<String, Double>()
    lateinit var email:String
    lateinit var password:String
    lateinit var dates:ArrayList<Dates>


    constructor(paramName:String, paramAddress:LatLng?, paramEmail:String, paramPassword:String, paramDates: ArrayList<Dates>){
        this.name=paramName;
        address.put("latitude", paramAddress!!.latitude)
        address.put("longitude", paramAddress!!.longitude)
        this.email=paramEmail
        this.password=paramPassword
        this.dates=paramDates
    }

    constructor()

}