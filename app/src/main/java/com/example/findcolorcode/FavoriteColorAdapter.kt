package com.example.findcolorcode

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findcolorcode.view.MainActivity.Companion.moshi
import com.example.findcolorcode.databinding.FavoriteColorListViewBinding
import com.example.findcolorcode.model.FavoriteColorDataClass

class FavoriteColorAdapter(
    /*
        private var context: Context,
        var colorArrayList: ArrayList<FavoriteColorDataClass>,
        private var childFragmentManager: FragmentManager,
        private val listener:baseOnColorClickListener= context as baseOnColorClickListener
    ) : RecyclerView.Adapter<FavoriteColorAdapter.HolderFavoriteColor>(),Filterable {

        private var originalList: ArrayList<FavoriteColorDataClass>
        private val TAG = "FavoriteColorAdapter"

        init {
            this.context = context
            this.colorArrayList = ArrayList(colorArrayList)
            this.originalList = ArrayList(colorArrayList)

            /*colorEditDialogを使用する時にFragmentMnagerが必要になるので呼び出し元の
             FavoriteColorFragmentからchildFragmentManagerを受け取る*/
            this.childFragmentManager = childFragmentManager
            Log.d(TAG, "$colorArrayList")
            colorArrayList.sortByDescending { it.editDate }
            originalList.sortByDescending { it.editDate }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFavoriteColor {
            val binding =
                FavoriteColorListViewBinding.inflate(LayoutInflater.from(context), parent, false)
            return HolderFavoriteColor(binding)
        }

        override fun getItemCount(): Int {
            return colorArrayList.size
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: HolderFavoriteColor, position: Int) {
            val model = colorArrayList[position]
            val colorName = model.colorName
            val colorCode = model.colorCode
            val colorSquare = Color.parseColor(colorCode)
            val colorMemo = model.colorMemo
            val editDate = model.editDate

            //データをviewHolderにセット
            holder.colorSquare.setBackgroundColor(colorSquare)
            holder.colorName.text = colorName
            holder.colorMemo.text = colorMemo
            holder.colorCode.text = colorCode
            holder.editDate.text = editDate

        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val sharedPreferences =
                        context.getSharedPreferences("favorite_colors", Context.MODE_PRIVATE)
                    originalList =
                        sharedPreferences.loadFavoriteColors(moshi) as ArrayList<FavoriteColorDataClass>

                    var filteredList = mutableListOf<FavoriteColorDataClass>()
                    Log.d(TAG, "filtered:$filteredList,origin:$originalList")

                    if (constraint.isNullOrEmpty()) {
                        filteredList.addAll(originalList)
                        Log.d(TAG, "FilterisisNullOrEmpty:$filteredList,$originalList")
                    } else {
                        for (item in originalList) {
                            if (
                                item.colorName.contains(constraint, true) ||
                                item.colorCode.contains(constraint, true) ||
                                item.colorMemo.contains(constraint, true) ||
                                item.editDate.contains(constraint, true)
                            ) {
                                filteredList.add(item)
                                Log.d(TAG, "FilterisnotemptyorNull:$filteredList,$originalList")
                            }
                        }
                    }
                    return FilterResults().apply {
                        values = filteredList
                        Log.d(TAG, "$values")
                    }
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    colorArrayList.clear()
                    colorArrayList.addAll(results?.values as List<FavoriteColorDataClass>)
                    notifyDataSetChanged()
                }
            }
        }

        inner class HolderFavoriteColor(binding: FavoriteColorListViewBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val colorSquare = binding.listColorSquare
            val colorListContainer = binding.colorListContainer
            val moreBtn = binding.moreBtn
            val colorName = binding.listColorName
            val colorMemo = binding.listColorMemo
            val colorCode = binding.listColorCode
            val editDate = binding.listEditDate

            init {
                colorListContainer.setOnLongClickListener {
                    popupMenu(colorName)
                    true
                }
                //削除、編集のポップアップメニューを表示
                moreBtn.setOnClickListener { view ->
                    popupMenu(view)
                    true
                }

            }

            private fun popupMenu(view: View) {
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.inflate(R.menu.more_menu_colorsavedialog)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {

                        R.id.baseOnColor ->{
                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                baseOnColor(adapterPosition)
                            }
                            true
                        }

                        R.id.delete -> {
                            val id = getNowPositionId()
                            if (id != null) {
                                removeFavoriteColor(id)
                            }
                            true
                        }

                        R.id.edit -> {
                            val id = getNowPositionId()
                            if (id != null) {
                                editFavoriteColor(id)
                            }
                            true
                        }

                        R.id.copyColorCode -> {
                            val id = getNowPositionId()
                            if (id != null) {
                                colorCodeIntoClipBoard(id)
                            }
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
            }

            private fun getNowPositionId(): String? {
                val position = adapterPosition
                return if (position != RecyclerView.NO_POSITION) {
                    colorArrayList[position].id
                } else {
                    null
                }
            }

            private fun removeFavoriteColor(nowPositionId: String) {
                val sharedPreferences =
                    context.getSharedPreferences("favorite_colors", Context.MODE_PRIVATE)
                val existingColors = sharedPreferences.loadFavoriteColors(moshi).toMutableList()

                //フィルターなしリストとフィルターリスト両方を検索
                val removedIndexInExistingColorlList =
                    existingColors.indexOfFirst { nowPositionId == it.id }
                val removedIndexInFilteredlList = colorArrayList.indexOfFirst { nowPositionId == it.id }

                //indexOfFirstはリストから見つからないときに-1を返す
                if (removedIndexInExistingColorlList != -1 && removedIndexInFilteredlList != -1) {
                    //RecyclerView表示用colorArrayListから削除する
                    colorArrayList.removeAt(removedIndexInFilteredlList)
                    //元データのexistingColorsから削除　sharePreferenceに保存
                    existingColors.removeAt(removedIndexInExistingColorlList)
                    sharedPreferences.saveFavoriteColors(moshi, existingColors)
                    notifyItemRemoved(removedIndexInFilteredlList)
                } else {
                    Log.e(TAG, "{$nowPositionId}がリストから見つかりません")
                }
            }
        }

        interface baseOnColorClickListener{
            fun deliveryColorChoiceFragment(colorCode: String)
        }
        private fun baseOnColor(position: Int) {
                val colorCode = colorArrayList[position].colorCode
                listener.deliveryColorChoiceFragment(colorCode)
        }

        private fun editFavoriteColor(nowPositionId: String) {
            val sharedPreferences =
                context.getSharedPreferences("favorite_colors", Context.MODE_PRIVATE)
            val existingColors = sharedPreferences.loadFavoriteColors(moshi).toMutableList()

            val editIndexInExistingColors = existingColors.indexOfFirst { nowPositionId == it.id }
            val editIndexInFilteredList = colorArrayList.indexOfFirst { nowPositionId == it.id }

            if (editIndexInExistingColors != -1 && editIndexInFilteredList != -1) {

                //各データを取得
                val editName = existingColors[editIndexInExistingColors].colorName
                val editMemo = existingColors[editIndexInExistingColors].colorMemo
                val editDate = existingColors[editIndexInExistingColors].editDate

                val dialog = ColorEditDialog().apply {
                    arguments = Bundle().apply {
                        putInt("editIndexInFilteredList", editIndexInFilteredList)
                        putInt("editIndexInExistingColors", editIndexInExistingColors)
                        putString("editName", editName)
                        putString("editMemo", editMemo)
                        putString("editDate", editDate)
                    }
                }
                dialog.show(childFragmentManager, "ColorEditDialog")
            }
        }

        fun colorCodeIntoClipBoard(nowPositionId: String) {
            val filtereredInIndex = colorArrayList.indexOfFirst { nowPositionId == it.id }
            if (filtereredInIndex != -1) {
                val saveColorCode = colorArrayList[filtereredInIndex].colorCode
                onCopy(context,saveColorCode)
            }
            Toast.makeText(context, "カラーコードを取得できません", Toast.LENGTH_SHORT).show()
        }

        fun sortNewDate() {
            colorArrayList.sortBy { it.editDate }
            notifyDataSetChanged()
        }
        fun oldNewDate() {
            colorArrayList.sortByDescending { it.editDate }
            notifyDataSetChanged()
        }
    }


    */
)