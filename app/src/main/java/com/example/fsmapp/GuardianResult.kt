package com.example.fsmapp

class GuardianResult {
    data class GuardianResult2(
        val response: Response
    )
    data class Response(
        val currentPage: Int,
        val orderBy: String,
        val pageSize: Int,
        val pages: Int,
        val results: List<Result>,
        val startIndex: Int,
        val status: String,
        val total: Int,
        val userTier: String
    )
    data class Result(
        val apiUrl: String,
        val fields: Fields,
        val id: String,
        val isHosted: Boolean,
        val pillarId: String,
        val pillarName: String,
        val sectionId: String,
        val sectionName: String,
        val type: String,
        val webPublicationDate: String,
        val webTitle: String,
        val webUrl: String
    )
    data class Fields(
        val bodyText: String,
        val standfirst: String
    )
}