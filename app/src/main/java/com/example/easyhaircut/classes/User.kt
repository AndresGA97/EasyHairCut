package com.example.easyhaircut.classes

import kotlin.collections.ArrayList

/**
 * User class
 * @author Andrés
 */
class User {
    lateinit var first:String
    lateinit var last:String
    lateinit var email:String
    lateinit var password:String
    lateinit var dates:ArrayList<Dates>


    constructor(name: String, lastName: String, email: String, password: String, paramDates:ArrayList<Dates>) {
        this.first = name
        this.last = lastName
        this.email = email
        this.password = password
        this.dates=paramDates
    }

    constructor()

}
