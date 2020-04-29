package com.example.easyhaircut.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.easyhaircut.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private var inflateView:View?=null
    private lateinit var name: TextView
    private val db:FirebaseFirestore= FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth= FirebaseAuth.getInstance()
    private lateinit var userRef:DocumentReference
    private var account: GoogleSignInAccount?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflateView= inflater.inflate(R.layout.fragment_profile, container, false)

        var intent=activity!!.intent
        account=intent.getParcelableExtra("accountGoogle")
        if(account!=null){
            userRef=db.collection("users").document(account!!.email.toString())
        }else{
            userRef=db.collection("users").document(auth.currentUser?.email.toString())
        }

        return inflateView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        name= inflateView!!.findViewById(R.id.textViewProfileName)
        loadData(inflateView!!)
    }

    private fun loadData(paramView:View){
        userRef.get()
            .addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
                if (documentSnapshot.exists()) {

                    var user: MutableMap<String, Any>? = documentSnapshot.data;
                    name.text = user!!.get("first").toString()
                } else {
                    Toast.makeText(
                        context,"Document does not exist",Toast.LENGTH_SHORT).show()
                }
            })
            .addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            })
    }
}
