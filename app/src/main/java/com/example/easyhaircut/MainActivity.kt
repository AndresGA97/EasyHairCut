package com.example.easyhaircut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toRegisterActivity(view: View) {
        var intent:Intent=Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }
}
