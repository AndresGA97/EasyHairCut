package com.example.easyhaircut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.easyhaircut.classes.User
import com.google.firebase.auth.FirebaseAuth

class InicialActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //Declare Firebase Auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)
        auth= FirebaseAuth.getInstance()
        var prueba=User("andres.garcia.alarcon.97@gmail.com")
        var prueba2=User("nombre","apelldo","email@gmai.com","123456")
//        Log.i("prueba","nombre: "+prueba.getName())
    }

    fun prueba(view: View) {

    }
}
