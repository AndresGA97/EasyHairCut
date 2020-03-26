package com.example.easyhaircut.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.easyhaircut.R

/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment() {
    private var inflateView:View?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflateView = inflater.inflate(R.layout.fragment_map, container, false)
        return inflateView
    }

}
