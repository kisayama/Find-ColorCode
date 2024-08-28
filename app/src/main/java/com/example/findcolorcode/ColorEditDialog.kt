package com.example.findcolorcode

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.set
import androidx.fragment.app.DialogFragment
import com.example.findcolorcode.databinding.FavoritecolorDialogBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.util.UUID

class ColorEditDialog: DialogFragment() {
    interface ColorEditListener {
        fun onColorEdit(editColorsIndex:Int,editedColor: FavoriteColorDataClass)
    }
    private val TAG = "ColorEditDialog"
    private var listener: ColorEditListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            parentFragment is ColorEditListener -> parentFragment as ColorEditListener
            context is ColorEditListener -> context as ColorEditListener
            else -> throw RuntimeException("$context は OnColorEditListener を実装する必要があります")
        }
        Log.d(TAG,("listener:$listener"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FavoritecolorDialogBinding.inflate(LayoutInflater.from(requireContext()))

        val editName = arguments?.getString("editName")
        val editMemo = arguments?.getString("editMemo")

        //入力フォームに前回までの名前とメモをセットしておく
        binding.favoritecolorMyName.setText(editName)
        binding.favoriteColorMemo.setText(editMemo)

        binding.favoritecolorMyName.requestFocus()


        return requireActivity().let {
            AlertDialog.Builder(it).apply {
                setTitle("保存した色を編集")
                setView(binding.root)
                setPositiveButton("OK", null)
                setNegativeButton("キャンセル") { dialog, _ ->
                    dialog.cancel()
                }
            }.create().apply {
                setOnShowListener { dialog ->
                    val positiveButton =
                        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                    positiveButton.setOnClickListener {
                        val editIndexInExistingColors = requireArguments().getInt("editIndexInExistingColors")
                        val editIndexInFilteredList = requireArguments().getInt("editIndexInFilteredList")

                        //入力された文字列
                        val favoriteColorMyName = binding.favoritecolorMyName.text.toString()
                        val favoriteColorMemo = binding.favoriteColorMemo.text.toString()

                        if (favoriteColorMyName.isEmpty()) {
                            binding.toUserMessage.text =
                                getString(R.string.ColorSaveDialog_form_edite1)
                            return@setOnClickListener
                        }

                        if (favoriteColorMemo.isEmpty()) {
                            binding.toUserMessage.text =
                                getString(R.string.ColorSaveDialog_form_edite2)
                            return@setOnClickListener
                        }
                        binding.toUserMessage.text = ""

                        val sharedPreferences = requireContext().getSharedPreferences(
                            "favorite_colors",
                            Context.MODE_PRIVATE
                        )

                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

                        var existingColors = sharedPreferences.loadFavoriteColors(moshi).toMutableList()

                        existingColors.let { list ->
                            if (editIndexInExistingColors in list.indices) {
                                existingColors[editIndexInExistingColors].colorName = favoriteColorMyName
                                existingColors[editIndexInExistingColors].colorMemo = favoriteColorMemo
                                existingColors[editIndexInExistingColors].editDate = getDate(LocalDateTime.now())
                            }
                        }
                        sharedPreferences.saveFavoriteColors(moshi,existingColors)
                        val editedColor = existingColors[editIndexInExistingColors]

                        dismiss()
                        listener?.onColorEdit(editIndexInFilteredList,editedColor)
                    }
                }
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}
