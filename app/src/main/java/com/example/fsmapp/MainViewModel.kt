package com.example.fsmapp

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel() : ViewModel() {
    private val search: MutableLiveData<String> = MutableLiveData("")
    fun getSearch(): String? {
        return search.value
    }
    fun setSearch(string: String) {
        search.value = string
    }
    private val docs: MutableLiveData<ArrayList<NYTResult2.Doc>> = MutableLiveData(ArrayList())
    fun getDocs(): ArrayList<NYTResult2.Doc>? {
        return docs.value
    }
    fun setDocs(arrayList: ArrayList<NYTResult2.Doc>) {
        docs.value = arrayList
    }
    fun getDocData(): MutableLiveData<ArrayList<NYTResult2.Doc>> {
        return docs
    }
}