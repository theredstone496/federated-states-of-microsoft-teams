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
    private val docs: MutableLiveData<ArrayList<NewsResult.Article>> = MutableLiveData(ArrayList())
    fun getDocs(): ArrayList<NewsResult.Article>? {
        return docs.value
    }
    fun setDocs(arrayList: ArrayList<NewsResult.Article>) {
        docs.value = arrayList
    }
    fun getDocData(): MutableLiveData<ArrayList<NewsResult.Article>> {
        return docs
    }
    private val sources: MutableLiveData<ArrayList<SourceItem>> = MutableLiveData(ArrayList())
    fun getSources(): ArrayList<SourceItem>? {
        return sources.value
    }
    fun getSourceData(): MutableLiveData<ArrayList<SourceItem>> {
        return sources
    }
}