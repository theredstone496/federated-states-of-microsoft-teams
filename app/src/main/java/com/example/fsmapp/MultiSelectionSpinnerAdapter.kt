package com.example.fsmapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

//obtained from stackoverflow lol

class MultiSelectionSpinnerAdapter(var context: Context?, var spinnerList: List<String>?, val multipleSelectionCallBack:MultipleSelectionCallBack) : BaseAdapter() {

    private var TAG = MultiSelectionSpinnerAdapter::class.java.simpleName
    private var selectedItemsList: ArrayList<String> = ArrayList()
    fun setSelectedItemsList(selectedItemsList: ArrayList<String>) {
        this.selectedItemsList = selectedItemsList
        notifyDataSetChanged()
    }
    private val inflater: LayoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_multi_selection_spinner, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        try {
            vh.tvSpinnerText.text = spinnerList!![position]
        }catch (e:Exception){
            e.printStackTrace()
        }

        if (selectedItemsList.size > 0) {
            for (i in selectedItemsList.indices) {
                if (selectedItemsList[i] == spinnerList!![position]) {
                    vh.llSpinnerSelected.visibility = View.VISIBLE
                    break
                }else{
                    vh.llSpinnerSelected.visibility = View.GONE
                }
            }
        }

        view.setOnClickListener {
            val selectedItemText = spinnerList!![position]
            if(vh.llSpinnerSelected.visibility == View.VISIBLE){
                vh.llSpinnerSelected.visibility = View.GONE
                multipleSelectionCallBack.onItemSelect(false, selectedItemText, position)
            }else{
                vh.llSpinnerSelected.visibility = View.VISIBLE
                multipleSelectionCallBack.onItemSelect(true, selectedItemText, position)
            }
        }
        return view
    }

    override fun getItem(position: Int): Any? {
        return spinnerList!![position]
    }

    override fun getCount(): Int {
        return spinnerList!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private class ItemHolder(row: View?) {
        val tvSpinnerText: TextView
        val llSpinnerSelected: LinearLayout

        init {
            tvSpinnerText = row?.findViewById(R.id.tvSpinnerText) as TextView
            llSpinnerSelected = row?.findViewById(R.id.llSpinnerSelected) as LinearLayout
            llSpinnerSelected.visibility = View.GONE
        }
    }

    interface MultipleSelectionCallBack{
        fun onItemSelect(isSelect:Boolean, selectedItemText:String, position: Int)
    }
}