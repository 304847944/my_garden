package com.liuchenxi.foundation.http.module

data class Response_JuheNews(
    var stat: String,
    var data: List<NewsMessage>
) {
    data class NewsMessage(
        var uniquekey: String,
        var title: String,
        var date: String,
        var category: String,
        var author_name: String,
        var url: String,
        var thumbnail_pic_s: String,
        var thumbnail_pic_s02: String,
        var thumbnail_pic_s03: String
    )
}