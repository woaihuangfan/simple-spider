package com.fan.spider

import cn.hutool.core.thread.ThreadUtil.sleep
import cn.hutool.core.util.PageUtil
import com.fan.client.NoticeDetailClient.fetchWebDetail
import com.fan.client.SearchClient
import com.fan.processor.NoticeProcessor.process
import com.fan.response.WebNoticeResponseSearchResult

private const val ROWS = 20

object WebSpider {
    fun useWebApi(keyword: String) {
        val total = ArrayList<WebNoticeResponseSearchResult>()
        var pageNumber = 1
        val response = SearchClient.searchWeb(keyword, pageNumber, ROWS)
        val hitsTotal = response.hitsTotal
        val totalPage = PageUtil.totalPage(hitsTotal, ROWS)
        for (i in 1..totalPage) {
            sleep(1000)
            total.add(SearchClient.searchWeb(keyword, i, ROWS))
        }
        total.add(response)
        val validResult = total.map { it.result.noticeWeb }.flatten()
//        while (validResult.size < 1 && pageNumber < totalPage) {
//            sleep(1000)
//            pageNumber++
//            total.add(SearchClient.searchWeb(keyword, pageNumber, ROWS))
//            val newResult = total.map { it.result.noticeWeb }.flatten().asSequence()
//                .filter { notice -> filterByMandatoryKeyWord(notice.title) }.toList()
//            validResult.addAll(newResult)
//        }
        var num =0
        validResult.forEach { noticeItem ->
            run {
                val code = noticeItem.code
                val detail = fetchWebDetail(code)
                if (detail.second.contains("会计") || detail.second.contains("审计")) {
                    if (detail.second.contains("事务所") && detail.second.contains("聘")) {
                        val accountCompanyName = process(detail.second, code)
                        if (!accountCompanyName.contains(code)) {
                            val data = detail.first.data
                            val stock = data?.security?.first()?.stock
                            val message =
                                "公司名称: ${noticeItem.securityFullName} 证券代码：$stock. 公告日期：${
                                    data?.notice_date?.substring(
                                        0,
                                        10
                                    )
                                } 会计公司：$accountCompanyName"
                            if (message.isNotEmpty()) {
                                num++
                                println(message)
                            }
                        }

                    }

                }

            }
        }
        println("成功提取数量：$num")
    }

}