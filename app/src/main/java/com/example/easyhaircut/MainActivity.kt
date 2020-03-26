package com.example.easyhaircut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.easyhaircut.exception.MissingDataException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //Declare Firebase Auth
    private val email: EditText by lazy { findViewById<EditText>(R.id.editTextUser) }
    private val password: EditText by lazy { findViewById<EditText>(R.id.editTextPassword) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth= FirebaseAuth.getInstance()//Initialize Firebase Auth
    }

    /**
     * Change the actual activity to registerActivity
     */
    fun toRegisterActivity(view: View) {
        var intent:Intent=Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    /**
     * change the actual activity to InicialActivity
     */
    fun toInicial(view: View) {
        if(email.text.toString()!=""&&password.text.toString()!=""){
            try{
                login()
            }catch (ex:MissingDataException){
                Toast.makeText(this,getString(R.string.missing_data),Toast.LENGTH_LONG).show()
            }
        }else{
            //Login error
            Toast.makeText(this, getString(R.string.missing_data),Toast.LENGTH_LONG).show()
            emptyField()
        }
    }

    /**
     * Login user in Firebase
     */
    fun login(){
        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this,
        OnCompleteListener { task -> if (task.isSuccessful){
            val user=auth.currentUser
            //Changing actual Activity
            var intent:Intent=Intent(this, InicialActivity::class.java)
            startActivity(intent)
        }else{
            //Login error
            Toast.makeText(this, getString(R.string.incorrect_data),Toast.LENGTH_SHORT).show()
            emptyField()
        }
        })
    }
    /**
     * Empty all editText from Register screen
     */
    private fun emptyField(){
        email.setText("")
        password.setText("")
    }
}
