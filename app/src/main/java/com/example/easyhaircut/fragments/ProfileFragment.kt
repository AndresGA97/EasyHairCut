package com.example.easyhaircut.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyhaircut.App.Companion.CHANNEL_1_ID
import com.example.easyhaircut.DateAdapter
import com.example.easyhaircut.DateItem
import com.example.easyhaircut.R
import com.example.easyhaircut.classes.Dates
import com.example.easyhaircut.classes.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), DateAdapter.OnDateListener {
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

    private lateinit var hairdresserName:String
    private lateinit var dates:ArrayList<Dates>

    //Notification
    private var notificationManager: NotificationManagerCompat? = null


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

        notificationManager = NotificationManagerCompat.from(context!!)

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
                    dates=user!!.get("dates") as ArrayList<Dates>
                    for (x in dates.indices){
                        var datesMap:HashMap<String, Timestamp> = dates[x] as HashMap<String, Timestamp>
                        var date:Date=datesMap["date"]!!.toDate()

                        var dateFormat= SimpleDateFormat("dd/MM/yyyy HH:mm")

                        //Compare dates for add to actual Dates or Recent Dates
                        if(date.after(now)){
                            var onlyDate=dateFormat.format(date)
                            var dateName:String=datesMap["name"].toString()
                            //add dates to list
                            actualDateList.add(DateItem(onlyDate,dateName, context!!))
                            hairdresserName=dateName
                        }else{
                            var onlyDate=dateFormat.format(date)
                            var dateName:String=datesMap["name"].toString()
                            //add dates to list
                            recentDateList.add(DateItem(onlyDate,dateName, context!!))
                            recentDateList[x].disableButton()
                        }

                        //inflate recyclerView actual dates
                        recyclerView=inflateView!!.findViewById(R.id.recycleViewActualDate)
                        recyclerView.setHasFixedSize(true)
                        layoutManager=LinearLayoutManager(context)
                        adapter=DateAdapter(actualDateList, this)
                        recyclerView.layoutManager=layoutManager
                        recyclerView.adapter=adapter


                        //inflate recyclerView recent dates
                        recyclerViewRecentDate=inflateView!!.findViewById(R.id.recyclerRecentDate)
                        recyclerViewRecentDate.setHasFixedSize(true)
                        layoutManagerRecentDate=LinearLayoutManager(context)
                        adapterRecentDate=DateAdapter(recentDateList, this)
                        recyclerViewRecentDate.layoutManager=layoutManagerRecentDate
                        recyclerViewRecentDate.adapter=adapterRecentDate

                        //Send notification if user have date in less than a day
                        if((date.time-86400000)>=now.time){
                            val notification= NotificationCompat.Builder(context!!, CHANNEL_1_ID)
                                .setSmallIcon(R.drawable.ic_home_black_24dp)
                                .setContentTitle(getString(R.string.near_date))
                                .setContentText(getString(R.string.next_date)+" "+dateFormat.format(date))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .build()
                            notificationManager!!.notify(1, notification)
                        }
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

    override fun onDeleteClick(position: Int) {
        try {
            actualDateList.removeAt(position)
            deleteNote(position)
            dates.removeAt((dates.size-1)-position)
            adapter.notifyItemRemoved(position)
        }catch (ex:Exception){
            Toast.makeText(context, R.string.delete_last_dates, Toast.LENGTH_SHORT).show()
        }

    }
    private fun deleteNote(position:Int){
        val userData=HashMap<String, Any>()
        userRef=db.collection("users").document(auth.currentUser!!.email!!)
        userRef.get().addOnSuccessListener{
            documentSnapshot ->
            if(documentSnapshot.exists()){
                var name:String=documentSnapshot["name"].toString()
                var lastName:String=documentSnapshot["last"].toString()
                var email:String=documentSnapshot["email"].toString()
                var password:String=documentSnapshot["password"].toString()
                var datesUser:ArrayList<Dates> = ArrayList<Dates>()
                var dateList:ArrayList<Dates> = documentSnapshot["dates"] as ArrayList<Dates>
                for (x in dateList.indices){
                    datesUser.add(dateList[x])
                }
                datesUser.removeAt((datesUser.size-1)-position)
                var user: User = User(name, lastName, email, password, datesUser)
                userData.put("name", user.name)
                userData.put("last", user.last)
                userData.put("email", user.email)
                userData.put("password", user.password)
                userData.put("dates", user.dates)

                userRef.set(userData)
            }
        }

        db.collection("hairdresser").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document["name"]==hairdresserName){
                        var hairdresserRef:DocumentReference=document.reference
                        var hairdresserData=document.data
                        var date=hairdresserData["dates"] as ArrayList<Dates>
                        var deleted=false
                        for (x in date.indices) {
                            var datesMap:HashMap<String, Timestamp> = date[x] as HashMap<String, Timestamp>

                            if (datesMap["name"].toString() == name.text&&!deleted) {
                                date.removeAt(x)
                                deleted=true
                            }
                        }
                        hairdresserRef.update("dates", date)
                    }
                }
            }
            .addOnFailureListener {
            }
    }
}
