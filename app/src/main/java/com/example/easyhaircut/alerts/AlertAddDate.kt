package com.example.easyhaircut.alerts

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.easyhaircut.R
import com.example.easyhaircut.classes.Dates
import com.example.easyhaircut.classes.Hairdresser
import com.example.easyhaircut.classes.User
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.hairdresser_item.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AlertAddDate(context: Context, year:Int, month:Int, dayOfMonth:Int, hairdresserEmail:String): DialogFragment() {
    val builder=AlertDialog.Builder(context, R.style.alert_dates)
    var title:String= " $dayOfMonth/$month/$year"
    val array = listDates(9,21,0)
    val year= year
    val month=month
    val dayOfMonth=dayOfMonth

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth=FirebaseAuth.getInstance()
    private var hairdresserRef =db.collection("hairdresser").document(hairdresserEmail)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder.setTitle(getString(R.string.add_date)+title).setItems(array,DialogInterface.OnClickListener{dialog, which ->
            var hour:String=(9+which).toString() //min hour plus which
            loadHairdresser(hour, year, month, dayOfMonth)
            //var dateList= arrayListOf<Dates>()
        })
        return builder.create()
    }

    /**
     * Create a list of dates for the hairdressers
     */
    fun listDates(min:Int, max:Int, interval:Int): Array<String> {
        var array:Array<String>
        var date:String=""
        for (x in min..max){
            if(x==max){
                date+="$x"
            }else{
                date+= "$x "
            }
        }
        array= date.split(" ").toTypedArray()
        return array
    }

    /**
     * Add new Date in Database user document
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun addDateHairdresserFromClient(hour:String, year:Int, month:Int, dayOfMonth:Int, hairdresser: Hairdresser, user:FirebaseUser){
        var date = "$year/$month/$dayOfMonth $hour"
        var localTime=LocalDateTime.of(year, month, dayOfMonth, Integer.parseInt(hour), 0)
        var millis= localTime.atZone(ZoneId.of("UTC+1")).toInstant().toEpochMilli()

        //add date to hairdresser Document
        val hairdresserData=HashMap<String, Any>()
        hairdresserData.put("name", hairdresser.name)
        hairdresserData.put("address", hairdresser.address)
        hairdresserData.put("email", hairdresser.email)
        hairdresserData.put("address", hairdresser.address)
        hairdresserData.put("password", hairdresser.password)
        hairdresser.dates.add(Dates(Date(millis), auth.currentUser!!.email!!))
        hairdresserData["dates"] = hairdresser.dates
        hairdresserRef.set(hairdresserData)

        //add date to User Document
        val userData=HashMap<String, Any>()
        var userRef=db.collection("users").document(user.email.toString())

        userRef.get().addOnSuccessListener { documentSnapshot ->
            if(documentSnapshot.exists()){
                var name:String=documentSnapshot["name"] as String
                var lastName:String=documentSnapshot["last"] as String
                var email:String=documentSnapshot["email"] as String
                var password:String=documentSnapshot["password"] as String
                var dates:ArrayList<Dates> = ArrayList<Dates>()
                var dateList:ArrayList<Dates> = documentSnapshot["dates"] as ArrayList<Dates>
                for (x in dateList.indices){
                    dates.add(dateList[x])
                }
                var user:User= User(name, lastName, email, password, dates)
                userData.put("name", user.name)
                userData.put("last", user.last)
                userData.put("email", user.email)
                userData.put("password", user.password)
                user.dates.add(Dates(Date(millis), hairdresser.name))
                userData.put("dates", user.dates)
                userRef.set(userData)
            }
        }
    }

    /**
     * Load user from database
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadHairdresser(hour:String, year:Int, month:Int, dayOfMonth:Int):Boolean {
        var success=false
        hairdresserRef.get()
            .addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    var name=documentSnapshot.get("name") as String
                    var address=documentSnapshot.get("address") as HashMap<String, Double>
                    var latLng= LatLng(address.get("latitude")!!, address.get("longitude")!!)
                    var email=documentSnapshot.get("email") as String
                    var password=documentSnapshot.get("password") as String
                    var dates:ArrayList<Dates> = ArrayList<Dates>()

                    var dateList:ArrayList<Dates> = documentSnapshot["dates"] as ArrayList<Dates>
                    for (x in dateList.indices){
                        dates.add(dateList[x])
                    }
                    var hairdresser = Hairdresser(name, latLng, email, password, dates)
                    addDateHairdresserFromClient(hour, year, month, dayOfMonth, hairdresser, auth.currentUser!!)
                    success=true
                } else {
                    success=false
                }
            })
            .addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

            })
        return success
    }
}
