package com.devatrii.dailynews.utils

const val POST_PER_PAGE = 15 // More posts more loading time
const val WEBSITE_URL = "https://muslimskeptic.com/"
const val BASE_URL = "$WEBSITE_URL/wp-json/wp/v2"
const val POSTS_URL = "$BASE_URL/posts?_embed&per_page=$POST_PER_PAGE&"
const val CATEGORIES_URL = "$BASE_URL/categories/"