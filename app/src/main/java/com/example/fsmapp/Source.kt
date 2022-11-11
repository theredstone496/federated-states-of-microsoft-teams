package com.example.fsmapp
class Source {
    data class SourceItem(
        val category: String,
        val country: String,
        val description: String,
        val id: String,
        val language: String,
        val name: String,
        val url: String
    )
    data class Source(val sources: ArrayList<SourceItem>)
}