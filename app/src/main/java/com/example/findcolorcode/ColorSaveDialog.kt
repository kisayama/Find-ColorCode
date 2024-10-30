package com.example.findcolorcode

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.findcolorcode.databinding.FavoritecolorDialogBinding
import com.example.findcolorcode.model.FavoriteColorDataClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.time.LocalDateTime
import java.util.UUID

class ColorSaveDialog : DialogFragment() {

    interface ColorSaveListener {
        fun onColorSaved(newColorIndex: Int, newColor: FavoriteColorDataClass)
    }

    /*private var listener: ColorSaveListener? = null
    private val TAG = "ColorSaveDialog"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            parentFragment is ColorSaveListener -> parentFragment as ColorSaveListener
            context is ColorSaveListener -> context
            else -> throw RuntimeException("$context は OnColorSaveListener を実装する必要があります")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FavoritecolorDialogBinding.inflate(layoutInflater)

        binding.favoritecolorMyName.requestFocus()

        return requireActivity().let {
            AlertDialog.Builder(it).apply {
                setTitle("お気に入りの色の情報")
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
                        val favoriteColorMyName = binding.favoritecolorMyName.text.toString()
                        val favoriteColorMemo = binding.favoriteColorMemo.text.toString()
                        val favoriteColorCode = arguments?.getString("colorCode")

                        if (favoriteColorMyName.isEmpty()) {
                            binding.toUserMessage.text =
                                getString(R.string.ColorSaveDialog_form_create1)
                            return@setOnClickListener
                        }
                        if (favoriteColorMemo.isEmpty()) {
                            binding.toUserMessage.text =
                                getString(R.string.ColorSaveDialog_form_create2)
                            return@setOnClickListener
                        }
                        binding.toUserMessage.text = ""

                        val sharedPreferences = requireContext().getSharedPreferences(
                            "favorite_colors",
                            Context.MODE_PRIVATE
                        )

                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

                        val newColor = if (favoriteColorCode != null) {
                            FavoriteColorDataClass(
                                UUID.randomUUID().toString(),
                                favoriteColorCode,
                                favoriteColorMyName,
                                favoriteColorMemo,
                                getDate(LocalDateTime.now())
                            )
                        } else {
                            Log.e(TAG, "favoriteColorCode is null")
                            return@setOnClickListener
                        }

                        val existingColors =
                            sharedPreferences.loadFavoriteColors(moshi).toMutableList()
                        val newColorIndex = existingColors.size

                        existingColors.add(newColor)

                        sharedPreferences.saveFavoriteColors(moshi, existingColors)

                        dismiss()
                        Toast.makeText(context, "保存完了", Toast.LENGTH_SHORT).show()

                        // リスナーのメソッドを呼び出して新しい色を通知
                        listener?.onColorSaved(newColorIndex,newColor)
                    }
                }

            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

*/
}