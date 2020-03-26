package com.example.easyhaircut.classes

import android.util.Log
import android.widget.Toast
import androidx.core.os.HandlerCompat.postDelayed
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

/**
 * User class
 * @author Andrés
 */
class User {
    private lateinit var name:String
    private lateinit var lastName:String
    private lateinit var email:String
    private lateinit var password:String
    private var age:Int=0
    private var genre:Boolean = false //false male, true female

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance() //Declare Firebase FireStore
    private var auth: FirebaseAuth= FirebaseAuth.getInstance()


    constructor(name: String, lastName: String, email: String, password: String, age:Int, genre:Boolean) {
        this.name = name
        this.lastName = lastName
        this.email = email
        this.password = password
        this.age=age
        this.genre=genre

        val user= hashMapOf("first" to name,
            "last" to lastName,
            "email" to email,
            "password" to password,
            "age" to age,
            "genre" to genre)

        //Insert user on fireStore
        db.collection("users")
            .document(this.email).set(user)
            .addOnSuccessListener { documentReference -> Log.d("Successful register",
                this.email+" Successful register") }
            .addOnFailureListener { exception -> Log.w("Failed",
                "Error adding document", exception) }

    }


    fun persist(){
        val user= hashMapOf("first" to name,
            "last" to lastName,
            "email" to email,
            "password" to password,
            "age" to age,
            "genre" to genre)

        db.collection("users").document(email).set(user)
    }
    fun getName():String{
        return this.name;
    }
}
