package com.example.easyhaircut.classes

import android.util.Log
import android.widget.Toast
import androidx.core.os.HandlerCompat.postDelayed
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.DelayQueue
import java.util.logging.Handler

class User {
    private lateinit var name:String
    private lateinit var lastName:String
    private lateinit var email:String
    private lateinit var password:String

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance() //Declare Firebase FireStore


    constructor(name: String, lastName: String, email: String, password: String) {
        this.name = name
        this.lastName = lastName
        this.email = email
        this.password = password

        val user= hashMapOf("first" to name,
            "last" to lastName,
            "email" to email,
            "password" to password)

        //Insert user on fireStore
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference -> Log.d("Successful register",
                "DocumentSnapshot added with ID: ${documentReference.id}") }
            .addOnFailureListener { exception -> Log.w("Failed",
                "Error adding document", exception) }

    }

    constructor(emailParam:String){
        db.collection("users").document(emailParam).get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val resultado=task.result
                this.name = resultado?.get("first").toString()
                Log.i("nombre",resultado?.get("first").toString())
                this.lastName = resultado?.get("last").toString()
                this.email = resultado?.get("email").toString()
                this.password = resultado?.get("password").toString()
            }
        }
    }

    fun persist(){
        val user= hashMapOf("first" to name,
            "last" to lastName,
            "email" to email,
            "password" to password)

        db.collection("users").document(email).set(user)
    }

    fun getName():String{return this.name}
}