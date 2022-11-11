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

}