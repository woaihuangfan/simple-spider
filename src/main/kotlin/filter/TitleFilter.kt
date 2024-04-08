package com.fan.filter

object TitleFilter {
     fun filterByMandatoryKeyWord(title: String) =
        title.contains("2024年度") && title.contains("会计师事务所")
}