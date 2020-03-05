package com.example.easyhaircut

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.easyhaircut.exception.MissingDataException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private val name:EditText by lazy { findViewById<EditText>(R.id.editTextNameRegister) }
    private val lastName:EditText by lazy { findViewById<EditText>(R.id.editTextLastNameRegister) }
    private val email:EditText by lazy { findViewById<EditText>(R.id.editTextEmailRegister) }
    private val email2:EditText by lazy { findViewById<EditText>(R.id.editTextEmail2Register) }
    private val password:EditText by lazy { findViewById<EditText>(R.id.editTextPasswordRegister) }
    private val password2:EditText by lazy { findViewById<EditText>(R.id.editTextPassword2Register) }
    private val userType:Switch by lazy { findViewById<Switch>(R.id.switchUserTypeRegister) } //false = User or True = hair dresser

    private lateinit var auth: FirebaseAuth //Declare Firebase Auth
    private lateinit var db:FirebaseFirestore //Declare Firebase FireStore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth= FirebaseAuth.getInstance()//Initialize Firebase Auth
        db= FirebaseFirestore.getInstance()
    }

    /**
     * Register and Login User (First Time)
     */
    fun authUser(){
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SuccessLogin", "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext, getString(R.string.authentication_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    registerUserDB()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ErrorLogin", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    throw MissingDataException(getString(R.string.missing_data))
                }
            }
        }
    /**
     * Register the user values on database
     */
    private fun registerUserDB() {
        val user= hashMapOf("first" to name.text.toString(),
            "last" to lastName.text.toString(),
            "email" to email.text.toString(),
            "password" to password.text.toString(),
            "userType" to if (userType.isChecked)true else false)

        //Insert user on fireStore
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference -> Log.d("Successful register",
                "DocumentSnapshot added with ID: ${documentReference.id}") }
            .addOnFailureListener { exception -> Log.w("Failed",
                "Error adding document", exception) }
        //Changing actualActivity
        var intent: Intent = Intent(this,InicialActivity::class.java)
        startActivity(intent)
    }

    /**
     * Change the Activity to InicialActivity
     */
    fun toInicialFromRegister(view: View) {
        if(name.text.toString()!="" && lastName.text.toString()!="" && email.text.toString()!="" && password.text.toString()!=""){
            if(password.text.toString().equals(password2.text.toString()) && email.text.toString().equals(email2.text.toString())){
                authUser()
            }else{
                Toast.makeText(this,getString(R.string.missing_data),Toast.LENGTH_LONG).show()
            }
        }
    }
}
