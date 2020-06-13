package com.example.easyhaircut.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.example.easyhaircut.R

/**
 * A simple [Fragment] subclass.
 */
class SettingsDatesFragment : Fragment() {
    private var inflateView:View?=null

    private lateinit var minEditText:EditText; private lateinit var maxEditText:EditText; private lateinit var intervalEditText: EditText; private lateinit var restEditText: EditText
    private lateinit var acceptButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflateView=inflater.inflate(R.layout.fragment_settings_dates, container, false)

        return inflateView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        minEditText= inflateView!!.findViewById(R.id.editTextMin)
        maxEditText= inflateView!!.findViewById(R.id.editTextMax)
        intervalEditText= inflateView!!.findViewById(R.id.editTextInterval)
        restEditText=inflateView!!.findViewById(R.id.editTextRest)
        acceptButton=inflateView!!.findViewById(R.id.buttonAccept)

        acceptButton.setOnClickListener { v ->
            saveDateSettings()
            Toast.makeText(context, R.string.change_saved, Toast.LENGTH_SHORT).show()
        }
    }


    fun saveDateSettings(){
        var preferences: SharedPreferences =activity!!.getSharedPreferences("dateSetting", Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor=preferences.edit()
        editor.putInt("min", minEditText.text.toString().toInt())
        editor.putInt("max", maxEditText.text.toString().toInt())
        editor.putInt("interval", intervalEditText.text.toString().toInt())
        editor.putInt("rest", restEditText.text.toString().toInt())
        editor.commit()
    }
}
