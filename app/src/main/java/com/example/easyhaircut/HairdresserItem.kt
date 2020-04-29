package com.example.easyhaircut

import android.content.Context
import android.widget.Button

class HairdresserItem {
    var hairdresserName:String
    var buttonNewDate:Button

     constructor(hairdresserName: String, context: Context) {
        this.hairdresserName = hairdresserName
        this.buttonNewDate= Button(context)
    }

}