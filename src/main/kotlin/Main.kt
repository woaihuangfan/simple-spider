package com.fan

import com.fan.response.NoticeResponseSearchResult
import com.google.gson.Gson
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

private val httpClient = OkHttpClient.Builder().build()
fun main() {
    val url =
        "https://choicegw.eastmoney.com/app/report/web/app/search/notice?keyWord=关于续聘会计师事所&startIndex=1&rows=200"
    try {
        val jsonResponse = getJSONResponse(url)

        val previewUrl = "http://pdf.dfcfw.com/pdf/DH2_%sv2_1.txt"
        jsonResponse.forEach { infoCode ->
            run {
                preview(previewUrl.format(infoCode))
            }
        }


    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun preview(url: String) {
    val request = Request.Builder().url(url).get().build()
    val response = httpClient.newCall(request).execute()
    println(response)
    val jsonString = response.body?.source()?.readUtf8()
    val map = Gson().fromJson(jsonString, Map::class.java)
    map.values.forEach { println(it) }
}

private fun getJSONResponse(url: String): List<String> {
    val rawHeaders = """
        Host: choicegw.eastmoney.com
        Connection: keep-alive
        Accept: */*
        User-Agent: app-iphone-client-iPhone14,5-BBD1331F-3385-4890-902B-CC87D317E20D
        Accept-Language: zh-CN,zh-Hans;q=0.9
        appid: UHc7QTQqgQe0JtUsK7cdWaBIrRJYmmsJ@MOBILEAPP
        Accept-Encoding: gzip
    """.trimIndent().lineSequence().toList()
    val request = Request.Builder().url(url).headers(headers(rawHeaders)).get().build()
    val response = httpClient.newCall(request).execute()
    val jsonString = response.body?.source()?.readUtf8()
    val (code, message, data) = Gson().fromJson(jsonString, NoticeResponseSearchResult::class.java)
    val infoCodes = data.searchResultList.filter { searchResult ->
        searchResult.title.contains("2024年度") && searchResult.title.contains("会计师事务所")
    }.map { searchResult -> searchResult.attach }.flatten()
        .filter { attach -> attach.infocode != null }
        .filter { it.infocode.isNotEmpty() }
        .map { it.infocode }
    return infoCodes
}

private fun headers(rawHeaders: List<String>): Headers {
    val map = rawHeaders.map {
        it.split(": ")
    }.flatten().toTypedArray()
    return Headers.headersOf(*map)
}