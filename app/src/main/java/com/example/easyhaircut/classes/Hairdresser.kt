package com.example.easyhaircut.classes

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Hairdresser class
 * @author Andrés
 */
class Hairdresser {
    private lateinit var name:String
    private lateinit var address:String
    private lateinit var email:String
    private lateinit var password:String

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance() //Declare Firebase FireStore

    constructor(paramName:String, paramAddress:String, paramEmail:String, paramPassword:String){
        this.name=paramName;
      
        this.address=paramAddress
        this.email=paramEmail
        this.password=paramPassword

        val hairdresser= hashMapOf("first" to name,
            "address" to address,
            "email" to email,
            "password" to password)
        //Insert hairdresser on fireStore
        db.collection("hairdresser")
            .document(this.email).set(hairdresser)
            .addOnSuccessListener { documentReference -> Log.d("Successful register",
                this.email+" Successful register") }
            .addOnFailureListener { exception -> Log.w("Failed",
                "Error adding document", exception) }
    }

    fun persist(){
        val hairdresser= hashMapOf("first" to name,
            "address" to address,
            "email" to email,
            "password" to password)

        db.collection("hairdresser").document(email).set(hairdresser)
    }
}