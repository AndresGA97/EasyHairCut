package com.example.easyhaircut.alerts

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.easyhaircut.R
import com.example.easyhaircut.classes.Dates
import com.example.easyhaircut.classes.Hairdresser
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class AlertAddDate(context: Context, year:Int, month:Int, dayOfMonth:Int, hairdresserName:String): DialogFragment() {
    val builder=AlertDialog.Builder(context, R.style.alert_dates)
    var title:String= " $dayOfMonth/$month/$year"
    val array = listDates(9,21,0)
    val name=hairdresserName

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var hairdresserRef: CollectionReference =db.collection("hairdresser")


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder.setTitle(getString(R.string.add_date)+title).setItems(array,DialogInterface.OnClickListener{dialog, which ->
            var hour:String=(9+which).toString() //min hour plus which
            Toast.makeText(context, hour, Toast.LENGTH_SHORT).show()
            //var dateList= arrayListOf<Dates>()
            //loadHairdressers(context!!, name)
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

    fun loadHairdressers(context: Context, hairdresserName: String){
        hairdresserRef.whereEqualTo("name", hairdresserName)
            .get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    //documentSnapshot["dates"]=
                }
            }).addOnFailureListener(OnFailureListener { e ->
                e.message
            })
    }
}
