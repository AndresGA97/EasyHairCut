package com.example.easyhaircut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class InicialActivity : AppCompatActivity() {
    private val email: TextView by lazy { findViewById<TextView>(R.id.textViewPrueba) }
    private lateinit var auth: FirebaseAuth //Declare Firebase Auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)
        auth= FirebaseAuth.getInstance()
        email.text= auth.currentUser?.email.toString()
    }
}
