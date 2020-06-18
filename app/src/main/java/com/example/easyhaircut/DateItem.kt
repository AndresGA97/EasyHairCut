package com.example.easyhaircut

import android.content.Context
import android.widget.Button

class DateItem {
    var date:String
    var name:String
    private var deleteButton:Button

    constructor(paramDate:String, paramName:String, context:Context){
        this.date=paramDate
        this.name=paramName
        this.deleteButton= Button(context)
        deleteButton.isEnabled=false
    }

    fun disableButton(){
        deleteButton.isEnabled=false
    }
}