package com.fan

import com.fan.spider.MobileSpider.useMobileApi
import com.fan.spider.WebSpider.useWebApi
import java.io.IOException


fun main() {

    try {
        println("==========开始爬取==========")
        val keyword = "续聘"
//        useMobileApi(keyword)
        useWebApi(keyword)
        println("==========爬取结束==========")
    } catch (e: IOException) {
        e.printStackTrace()
    }

}




