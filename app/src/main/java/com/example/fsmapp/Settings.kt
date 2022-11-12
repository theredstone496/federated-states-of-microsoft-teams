package com.example.fsmapp

class Settings {
    companion object {
        var sortBy: String ="relevancy"
        var query: String = ""
        var sources: ArrayList<Source.SourceItem> = ArrayList()
        var langs = arrayListOf(true, true,true,true,true,true,true,true,true,true,true,true,true,true)
        var existentlangs = arrayListOf("en", "no", "it", "ar", "ud", "de", "pt", "es", "fr", "he", "ru", "sv", "nl", "zh")
        var searchOption: String = "match"
        var searchLocations = arrayListOf(true, true, true)
        var apikey = ""
    }
}