package com.example.easyhaircut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.easyhaircut.classes.User
import com.example.easyhaircut.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InicialActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //Declare Firebase Auth
    private lateinit var db: FirebaseFirestore //Declare Firebase FireStore
    private lateinit var actualUser: User

    private val manager: FragmentManager by lazy { this.supportFragmentManager }
    private val homeFragment: HomeFragment by lazy { HomeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)

        var transaction1: FragmentTransaction = manager.beginTransaction()
        transaction1.replace(R.id.FragmentContainer, homeFragment, "homeFragment")
        transaction1.addToBackStack("HomeFragment")
        transaction1.commit()

        auth= FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()
        var documentUser=db.collection("users").document(auth.currentUser!!.email.toString())

        documentUser.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val resultado=task.result
                var name = resultado?.get("first").toString()
                var lastName = resultado?.get("last").toString()
                var email = resultado?.get("email").toString()
                var password = resultado?.get("password").toString()
                actualUser=User(name, lastName, email, password)
            }
        }
        try{
            println(actualUser.getName())
        }catch (ex:Exception){

        }
    }

}
