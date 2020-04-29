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
    var address:LatLng? = null
    lateinit var email:String
    lateinit var password:String
    lateinit var dates:ArrayList<Dates>


    constructor(paramName:String, paramAddress:LatLng?, paramEmail:String, paramPassword:String, paramDates: ArrayList<Dates>){
        this.name=paramName;
        this.address=paramAddress
        this.email=paramEmail
        this.password=paramPassword
        this.dates=paramDates
    }
    constructor()

}