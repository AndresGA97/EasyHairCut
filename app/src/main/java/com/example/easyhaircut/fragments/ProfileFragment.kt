package com.example.easyhaircut.fragments

import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyhaircut.*
import com.example.easyhaircut.R
import com.example.easyhaircut.classes.Dates
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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

    private lateinit var recyclerView: RecyclerView;
    private lateinit var recyclerViewRecentDate: RecyclerView
    private lateinit var adapter: DateAdapter
    private lateinit var adapterRecentDate: DateAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var layoutManagerRecentDate: RecyclerView.LayoutManager
    private lateinit var actualDateList:ArrayList<DateItem>
    private lateinit var recentDateList:ArrayList<DateItem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflateView= inflater.inflate(R.layout.fragment_profile, container, false)
        actualDateList=ArrayList()
        recentDateList=ArrayList()

        var intent=activity!!.intent
        account=intent.getParcelableExtra("accountGoogle")
        if(account!=null){
            userRef=db.collection("users").document(account!!.email.toString())
        }else{
            userRef=db.collection("users").document(auth.currentUser?.email.toString())

        }

        return inflateView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        name= inflateView!!.findViewById(R.id.textViewProfileName)
        if(loadData(inflateView!!)){
            //Normal User
        }else{
            userRef=db.collection("hairdresser").document(auth.currentUser?.email.toString())
            loadData(inflateView!!)
        }

    }

    /**
     * Load data user from FireStore
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(paramView:View):Boolean{
        var success=false
        var now:Date=Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    var user: MutableMap<String, Any>? = documentSnapshot.data;
                    name.text = user!!.get("name").toString()
                    var dates=user!!.get("dates") as ArrayList<Dates>
                    for (x in dates.indices){
                        var datesMap:HashMap<String, Timestamp> = dates[x] as HashMap<String, Timestamp>
                        var date:Date=datesMap["date"]!!.toDate()

                        var dateFormat= SimpleDateFormat("dd/MM/yyyy HH:mm")
                        if(date.after(now)){
                            var onlyDate=dateFormat.format(date)
                            var dateName:String=datesMap["name"].toString()
                            //add dates to list
                            actualDateList.add(DateItem(onlyDate,dateName))
                        }else{
                            var onlyDate=dateFormat.format(date)
                            var dateName:String=datesMap["name"].toString()
                            //add dates to list
                            recentDateList.add(DateItem(onlyDate,dateName))
                        }

                        //inflate recyclerView actual dates
                        recyclerView=inflateView!!.findViewById(R.id.recycleViewActualDate)
                        recyclerView.setHasFixedSize(true)
                        layoutManager=LinearLayoutManager(context)
                        adapter=DateAdapter(actualDateList)
                        recyclerView.layoutManager=layoutManager
                        recyclerView.adapter=adapter

                        //inflate recyclerView actual dates
                        recyclerViewRecentDate=inflateView!!.findViewById(R.id.recyclerRecentDate)
                        recyclerViewRecentDate.setHasFixedSize(true)
                        layoutManagerRecentDate=LinearLayoutManager(context)
                        adapterRecentDate=DateAdapter(recentDateList)
                        recyclerViewRecentDate.layoutManager=layoutManagerRecentDate
                        recyclerViewRecentDate.adapter=adapterRecentDate

                    }
                    success=true
                } else {
                    success=false
                }
            }
            .addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            })
        return success
    }


}
