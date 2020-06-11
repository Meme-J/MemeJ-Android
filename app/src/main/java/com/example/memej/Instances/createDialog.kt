package com.example.memej.Instances

import android.app.AlertDialog
import com.example.memej.Utils.ApplicationUtil

fun createDialog(message: String): AlertDialog.Builder {

    val ctx = ApplicationUtil.getContext()
    val dialog = AlertDialog.Builder(ctx)
    dialog.setMessage(message)
    dialog.setPositiveButton(android.R.string.ok) { _, _ -> }

    return dialog
}