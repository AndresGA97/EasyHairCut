package com.example.easyhaircut.classes

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class User {
    private lateinit var name:String
    private lateinit var lastName:String
    private lateinit var email:String
    private lateinit var password:String

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance() //Declare Firebase FireStore
    constructor()

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

    fun searchUser(emailParam:String):User{
        lateinit var user:User
        db.collection("users").document(emailParam).get().addOnSuccessListener {
            documentSnapshot -> user=documentSnapshot.toObject(User::class.java) as User
        }
        return user
    }

    fun persist(){
        val user= hashMapOf("first" to name,
            "last" to lastName,
            "email" to email,
            "password" to password)

        db.collection("users").document(email).set(user)
    }

    fun getName():String{
        return this.name
    }
    fun setName(nameParam:String){
        this.name=nameParam
        persist()
    }
}