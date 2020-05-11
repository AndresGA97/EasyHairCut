package com.example.easyhaircut.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyhaircut.HairdresserAdapter
import com.example.easyhaircut.HairdresserItem
import com.example.easyhaircut.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_hairdresser_dates.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), HairdresserAdapter.OnHairdresserListener {
    private var inflateView:View?=null
    private var transaction1: FragmentTransaction?=null
    private val fragmentDates:HairdresserDatesFragment by lazy { HairdresserDatesFragment() }

    private lateinit var recyclerView:RecyclerView;
    private lateinit var adapter:HairdresserAdapter
    private lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var hairdresserList:ArrayList<HairdresserItem>

    private var db:FirebaseFirestore= FirebaseFirestore.getInstance()
    private var hairdresserRef:CollectionReference=db.collection("hairdresser")

    var name:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflateView= inflater.inflate(R.layout.fragment_home, container, false)
        hairdresserList=ArrayList<HairdresserItem>()

        return inflateView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadHairdressers(context!!)
    }

    /**
     * Function of recyclerView Click
     */
    override fun hairdresserClick(position: Int) {
        Toast.makeText(context, "Clicked "+hairdresserList.get(position).hairdresserName, Toast.LENGTH_SHORT).show()
        saveHairdresserName(position)
        var fragmentManager: FragmentManager? = fragmentManager
        if (fragmentManager != null) {
            transaction1=fragmentManager.beginTransaction()
        }
        transaction1!!.replace(R.id.FragmentContainer,fragmentDates,"fragmentDates")
        transaction1!!.addToBackStack("fragmentDates")
        transaction1!!.commit()

    }

    private fun loadHairdressers(context:Context){
        hairdresserRef.get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    name=documentSnapshot["name"].toString()

                    hairdresserList.add(HairdresserItem(name, context))
                    recyclerView=inflateView!!.findViewById(R.id.recyclerView_hairdresser)
                    recyclerView.setHasFixedSize(true)
                    layoutManager=LinearLayoutManager(context)
                    adapter= HairdresserAdapter(hairdresserList, this)
                    recyclerView.layoutManager=layoutManager
                    recyclerView.adapter=adapter
                }
            })
    }
    private fun saveHairdresserName(pos:Int){
        var preferences: SharedPreferences =activity!!.getSharedPreferences("actualHairdresserClicked", Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor=preferences.edit()
        editor.putString("name", hairdresserList.get(pos).hairdresserName)
        editor.commit()
    }

}
