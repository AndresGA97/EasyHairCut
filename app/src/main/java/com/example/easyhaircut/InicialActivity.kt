package com.example.easyhaircut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    fun prueba(view: View) {
        var prueba:User=User()
        prueba=prueba.searchUser("andres.garcia.alarcon.97@gmail.com")
        println(prueba.getName())
    }
}
