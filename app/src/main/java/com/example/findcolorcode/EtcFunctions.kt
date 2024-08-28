package com.example.findcolorcode

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.findcolorcode.databinding.ProgressDialogBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

fun showProgressDialog(context: Context):AlertDialog {
    val binding = ProgressDialogBinding.inflate(LayoutInflater.from(context))
    val builder = AlertDialog.Builder(context).apply {
        setView(binding.root)
        setCancelable(false)
        Log.d("showProgressDialog","showProgressDialog")
    }.create()

    builder.show()
    return builder
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDate(date: LocalDateTime):String {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    Log.d("ETCFUNctions","${date.format(formatter)}")
    return date.format(formatter)
}

fun onCopy(context: Context,copiedText: String) {
    val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("CopiedText", copiedText)
    clipboard.setPrimaryClip(clipData)
    Toast.makeText(
        context,
        "カラーコードをクリップボードにコピーしました",
        Toast.LENGTH_SHORT
    ).show()
}
fun onCut(context: Context,copiedText: String,textView: TextView){
    val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("CopiedText", copiedText)
    clipboard.setPrimaryClip(clipData)

    textView.text = ""

    Toast.makeText(
        context,
        "カラーコードをクリップボードにコピーしました",
        Toast.LENGTH_SHORT
    ).show()
}