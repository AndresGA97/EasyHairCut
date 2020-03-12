package com.example.easyhaircut.classes

import android.util.Log
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
            .document(this.email).set(user)
            .addOnSuccessListener { documentReference -> Log.d("Successful register",
                this.email+" Successful register") }
            .addOnFailureListener { exception -> Log.w("Failed",
                "Error adding document", exception) }

    }

    constructor(emailParam:String){
        db.collection("users").document(emailParam).get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val result=task.result
                this.name = result?.get("first").toString()
                Log.i("nombre",result?.get("first").toString())
                this.lastName = result?.get("last").toString()
                Log.i("Apellidos", result?.get("last").toString())
                this.email = result?.get("email").toString()
                Log.i("email",result?.get("email").toString())
                this.password = result?.get("password").toString()
                Log.i("password",result?.get("password").toString())
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