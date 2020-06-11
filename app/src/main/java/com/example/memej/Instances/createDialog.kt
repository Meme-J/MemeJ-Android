package com.example.memej.Instances

import android.app.AlertDialog
import android.content.Context

fun createDialog(message: String, ctx: Context): AlertDialog.Builder {

    val dialog = AlertDialog.Builder(ctx)
    dialog.setMessage(message)
    dialog.setPositiveButton(android.R.string.ok) { _, _ -> }

    return dialog
}