package com.example.easyhaircut

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.easyhaircut.classes.Dates
import com.example.easyhaircut.classes.Hairdresser
import com.example.easyhaircut.classes.User
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


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
    private lateinit var userRef:DocumentReference
    private lateinit var hairdresserRef:DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //animation Background gradient
        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintRegister)
        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

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
                    //Check if we are registering like hairdresser or user
                    if(userType.isChecked){
                        hairdresserRef=db.collection("hairdresser").document(email.text.toString());
                        registerHairDresserDB()
                    }else{
                        userRef=db.collection("users").document(email.text.toString());
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

        var user:User= User(name.text.toString(),lastName.text.toString(),email.text.toString(), password.text.toString(),ArrayList<Dates>())

        //Insert user on fireStore
        userRef.set(user)
            .addOnSuccessListener { documentReference ->  }
            .addOnFailureListener { }

        var preferences: SharedPreferences =getSharedPreferences("userType", Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor=preferences.edit()
        editor.putBoolean("user", true)
        editor.commit()

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
        var address:LatLng?=getLocationFromAddress(this, lastName.text.toString())
        var hairdresser:Hairdresser= Hairdresser(name.text.toString(), address,email.text.toString(),password.text.toString(), ArrayList<Dates>())


        //Insert user on fireStore
       hairdresserRef.set(hairdresser)
            .addOnSuccessListener { documentReference -> }
            .addOnFailureListener { }

        var preferences: SharedPreferences =getSharedPreferences("userType", Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor=preferences.edit()
        editor.putBoolean("user", false)
        editor.commit()

        //Changing actualActivity
        var intent: Intent = Intent(this,InicialActivity::class.java)
        startActivity(intent)
    }

    /**
     * Return latLng object from String address
     */
    fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1)
            if (address == null) {
                return null
            }
            val location = address[0]
            p1 = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }
}
