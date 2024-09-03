package com.example.findcolorcode

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findcolorcode.databinding.FragmentFavoriteColorBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class FavoriteListFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentFavoriteColorBinding? = null
    private var adapter: FavoriteColorAdapter? = null
    private val binding get() = _binding!!
    private val TAG = "FavoriteColorFragment"

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteListFragment()
    }
    //色の追加
    fun addNewColor(newColorIndex: Int,newColor: FavoriteColorDataClass?) {
        adapter?.let {
            if (newColor != null){
            it.colorArrayList.add(newColor)
            }
            it.notifyItemInserted(newColorIndex)
            Log.d(TAG,"$adapter")
        }?: Log.e(TAG, "Adapter is not initialized")
    }
    //色の編集
    fun editColor(editColorIndex: Int,editedColor: FavoriteColorDataClass) {
        adapter?.let {
            it.colorArrayList[editColorIndex] = editedColor
            it.notifyItemChanged(editColorIndex)
            Log.d(TAG,"$adapter")
        }?: Log.e(TAG, "Adapter is not initialized")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteColorBinding.inflate(inflater, container, false)
        recyclerView = binding.favoriteColorListRecyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(
            "favorite_colors",
            Context.MODE_PRIVATE
        )

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val readFavoriteColorList =
            sharedPreferences.loadFavoriteColors(moshi).toCollection(ArrayList())

        adapter = FavoriteColorAdapter(requireContext(), readFavoriteColorList,childFragmentManager)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter

        //filterの設定
        binding.searchEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                adapter?.filter?.filter(s.toString())
            }
        })
        //SortBtnからメニューを表示
        binding.sortBtn.setOnClickListener { view->
            popupSortMenu(view)
            true
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun popupSortMenu(view: View){
        val popupMenu = PopupMenu(view.context,view)
        popupMenu.inflate(R.menu.sort_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.sort_new ->{
                    adapter?.sortNewDate()
                    true
                }

                R.id.sort_old ->{
                    adapter?.oldNewDate()
                    true
                }
                else->false
            }
        }
        popupMenu.show()
    }
}
