package com.devatrii.dailynews.Models

import com.devatrii.dailynews.LAYOUT_TYPE_SETTING
import java.io.Serializable

data class ArticleModel(
    val id: Int, // id
    val date: String, // date
    val title: String, // yoast_head_json => title
    val content: String, // content => rendered
    val excerpt: String, // excerpt => rendered
    val author_name: String, // yoast_head_json => author
    val author_url: String, // author[0] => href
    val author_pic: String, // author[0] => href
    val reading_time: String, // twitter_misc => Est. reading time"
    val link: String, // link
    val image: String, //og_image=>url
    val categories: Int, // categories
    var LAYOUT_TYPE:Int = LAYOUT_TYPE_SETTING
) : Serializable