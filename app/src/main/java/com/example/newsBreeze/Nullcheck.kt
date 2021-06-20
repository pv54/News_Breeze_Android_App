package com.example.newsBreeze

object NullCheck {
    fun check(newslist: ArrayList<Article>): ArrayList<Article> {
        for (i in newslist.indices) {
            val newsl=newslist[i]
            if (newsl.author == null) {
                newsl.author = "Author Not Available"
            }
            if (newsl.urlToImage == null) {
                newsl.urlToImage = "Invalid"
            }
            if (newsl.url == null) {
                newsl.url = "Invalid"
            }
            if (newsl.description == null) {
                newsl.description = "Description Not Available"
            }
            if (newsl.title == null) {
                newsl.title = "Title Not Available"
            }
            if (newsl.publishedAt == null) {
                newsl.publishedAt = "Date Not Available"
            }
        }
        return newslist
    }


}