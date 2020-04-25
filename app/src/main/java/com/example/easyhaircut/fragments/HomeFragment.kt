package com.example.easyhaircut.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.easyhaircut.R
import com.synnapps.carouselview.CarouselView


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private var inflateView:View?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflateView= inflater.inflate(R.layout.fragment_home, container, false)

        return inflateView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}
