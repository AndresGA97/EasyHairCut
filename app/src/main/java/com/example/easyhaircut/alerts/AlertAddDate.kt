package com.example.easyhaircut.alerts

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.easyhaircut.R

class AlertAddDate(context: Context): DialogFragment() {
    val builder=AlertDialog.Builder(context, R.style.alert_dates)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder.setTitle(R.string.add_date)
        builder.setMessage(R.string.add_date_description)
        return builder.create()
    }
}