package com.example.findcolorcode

import android.content.Context
import android.view.ActionMode
import android.view.ActionMode.Callback
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView


class CustomActionMode(private val context: Context, private val textView: TextView): ActionMode.Callback {
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.actionmode_menu,menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        val selectedText = textView.text.toString()
        when(item?.itemId) {
            R.id.copy -> {
                onCopy(context,selectedText)
                return true
            }

            R.id.cut -> {
                onCut(context,selectedText,textView)
                return true
            }
        }
            return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {}

}