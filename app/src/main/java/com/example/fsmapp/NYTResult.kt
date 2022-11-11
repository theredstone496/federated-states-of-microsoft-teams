package com.example.fsmapp

import com.beust.klaxon.Json

data class NYTResult(
    @Json(name = "status")
    val status: String,
    @Json(name = "copyright")
    val copyright: String,
    @Json(name = "response")
    val response: String
)