package com.example.easyhaircut

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.easyhaircut.exception.MissingDataException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class RegisterActivity : AppCompatActivity() {
    private val name:EditText by lazy { findViewById<EditText>(R.id.editTextNameRegister) }
    private val lastName:EditText by lazy { findViewById<EditText>(R.id.editTextLastNameRegister) }
    private val email:EditText by lazy { findViewById<EditText>(R.id.editTextEmailRegister) }
    private val email2:EditText by lazy { findViewById<EditText>(R.id.editTextEmail2Register) }
    private val password:EditText by lazy { findViewById<EditText>(R.id.editTextPasswordRegister) }
    private val password2:EditText by lazy { findViewById<EditText>(R.id.editTextPassword2Register) }
    private val userType:Switch by lazy { findViewById<Switch>(R.id.switchUserTypeRegister) } //false = User or True = hair dresser
    private val info:TextView by lazy { findViewById<TextView>(R.id.textViewInfoError) }
    private val textViewLastName:TextView by lazy { findViewById<TextView>(R.id.textViewLastNameRegister) }

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
    private fun authUser(){
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SuccessLogin", "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext, getString(R.string.authentication_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    //Check if we are registering like hairdresser or user
                    if(userType.isChecked){
                        registerHairDresserDB()
                    }else{
                        registerUserDB()
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ErrorLogin", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, getString(R.string.incorrect_data),
                        Toast.LENGTH_SHORT
                    ).show()
                    //if fail, emptied the data fieldText
                    emptyField()
                    info.visibility = TextView.VISIBLE
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
            "password" to password.text.toString())

        //Insert user on fireStore
        db.collection("users")
            .document(email.text.toString()).set(user)
            .addOnSuccessListener { documentReference ->  }
            .addOnFailureListener { }
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
                //Register error
                emptyField()
                info.visibility=TextView.VISIBLE
            }
        }
    }

    /**
     * Empty all editText from Register screen
     */
    private fun emptyField(){
        name.setText("")
        lastName.setText("")
        email.setText("")
        email2.setText("")
        password.setText("")
        password2.setText("")
        userType.isChecked=false
    }

    /**
     * modify the register form between user or hairdresser
     */
    fun onClickTypeUser(view: View) {
        if(userType.isChecked){
            userType.setText(getString(R.string.hairdresser))
            textViewLastName.setText(getString(R.string.address))

        }else if(!userType.isChecked){
            userType.setText(getString(R.string.user))
            textViewLastName.setText(getString(R.string.last_name))
        }
    }

    /**
     * Register the hair dresser values on database
     */
    private fun registerHairDresserDB() {
        val user= hashMapOf("first" to name.text.toString(),
            "address" to lastName.text.toString(),
            "email" to email.text.toString(),
            "password" to password.text.toString())

        //Insert user on fireStore
        db.collection("hairdresser")
            .document(email.text.toString()).set(user)
            .addOnSuccessListener { documentReference -> }
            .addOnFailureListener { }
        //Changing actualActivity
        var intent: Intent = Intent(this,InicialActivity::class.java)
        startActivity(intent)
    }
}
