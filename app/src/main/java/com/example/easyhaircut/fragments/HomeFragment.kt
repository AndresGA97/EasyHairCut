package com.example.easyhaircut.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyhaircut.HairdresserAdapter
import com.example.easyhaircut.HairdresserItem
import com.example.easyhaircut.R
import com.synnapps.carouselview.CarouselView


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), HairdresserAdapter.OnHairdresserListener {
    private var inflateView:View?=null
    private lateinit var recyclerView:RecyclerView;
    private lateinit var adapter:HairdresserAdapter
    private lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var hairdresserList:ArrayList<HairdresserItem>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflateView= inflater.inflate(R.layout.fragment_home, container, false)
        hairdresserList=ArrayList<HairdresserItem>()
        hairdresserList.add(HairdresserItem("hola", context!!))
        hairdresserList.add(HairdresserItem("adios", context!!))

        return inflateView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView=inflateView!!.findViewById(R.id.recyclerView_hairdresser)
        recyclerView.setHasFixedSize(true)
        layoutManager=LinearLayoutManager(context)
        adapter= HairdresserAdapter(hairdresserList, this)
        recyclerView.layoutManager=layoutManager
        recyclerView.adapter=adapter

    }

    /**
     * Function of recyclerView Click
     */
    override fun hairdresserClick(position: Int) {
        Toast.makeText(context, "Clicked "+position, Toast.LENGTH_SHORT).show()


    }
}
