package com.fan.processor

object NoticeProcessor {
    fun extractCompanyName(notice: String) {
        val companyCode = notice.substringAfter("证券代码：").substringBefore("证券简称：")
        val companyName = getCompanyName(notice)
        val accountCompanyName = getAccountCompanyName(notice, "")
        println("%s(证券代码: %s):%s".format(companyName, companyCode, accountCompanyName))
    }

    fun process(notice: String, code: String): String {
        return getAccountCompanyName(notice, code)
    }

    private fun getAccountCompanyName(notice: String, code: String): String {
        var result = notice.substringAfter("会计师事务所名称：").substringBefore("成立日期：")
        if (result.length > 30 || result.length < 5) {
            result = notice.substringAfter("机构名称：").substringBefore("成立日期：")
        }
        if (result.length > 30 || result.length < 5) {
            result = notice.substringAfter("同意续聘").substringBefore("为公司 ")
        }
        if (result.length > 30 || result.length < 5) {
            result = notice.substringAfter("续聘").substringBefore("为")
        }
        if(!result.contains("事务所")){
            result = "解析失败 code:$code"
        }
        if(!result.trim().endsWith("）")){
            result = "解析失败 code:$code"
        }
        if (result.length > 30) {
//            println("====无法提取会计公司名称 code:$code====")
//            println("====公告内容如下====")
//            println(notice)
//            println("====公告内容结束====")
            result = "解析失败 code:$code"
        }
        return result
    }

    private fun getCompanyName(notice: String): String {
        var result = notice.substringAfter("证券简称：").substringBefore("公告编号：")
        if (result.length > 10 || result.length < 3) {
            result = notice.substringAfter("证券简称：").substringBefore("主办券商：")
        }
        if (result.length > 10 || result.length < 3) {
            println("====无法提取公司名称====".format(result))
        }
        return result
    }
}