package com.example.easyhaircut


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.easyhaircut.exception.MissingDataException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //Declare Firebase Auth
    private val email: EditText by lazy { findViewById<EditText>(R.id.editTextUser) }
    private val password: EditText by lazy { findViewById<EditText>(R.id.editTextPassword) }
    private lateinit var mGoogleSignInClient:GoogleSignInClient
    private lateinit var signInButton:SignInButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signInButton=findViewById(R.id.buttonGoogleSignIn)//Google sign in button
        auth= FirebaseAuth.getInstance()//Initialize Firebase Auth
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
        var account=GoogleSignIn.getLastSignedInAccount(this)
        signInButton.setOnClickListener(View.OnClickListener {
            signIn()
        })
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

    /**
     * Launch the registration Intent with google
     */
    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1)
    }

    /**
     * this method shows the selection google accounts task
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    /**
     *This method change the activity and pass through the intent the actual account
     */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            var intent:Intent=Intent(this, InicialActivity::class.java)
            intent.putExtra("accountGoogle",account)
            startActivity(intent)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GoogleLogin", "signInResult:failed code=" + e.statusCode)
        }
    }

}
