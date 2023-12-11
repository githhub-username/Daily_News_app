package com.devatrii.dailynews.repository

import android.content.Context
import androidx.core.text.HtmlCompat
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request.Method
import com.android.volley.toolbox.StringRequest
import com.devatrii.dailynews.Models.ArticleModel
import com.devatrii.dailynews.utils.POSTS_URL
import com.devatrii.dailynews.utils.VolleySingleton
import com.devatrii.dailynews.utils.logInfo
import org.json.JSONArray

class MainRepository(private val context: Context) {
    private val articlesList = MutableLiveData<APIResponses<ArrayList<ArticleModel>>>()
    val articleLiveData get() = articlesList

    fun getArticles(page: String) {
        articleLiveData.value = APIResponses.Loading()
        val url = "$POSTS_URL&page=$page"
        val requestQueue = VolleySingleton.getInstance(context).requestQueue
        val stringRequest = StringRequest(Method.GET, url, {
            if (it != null) {
                val tempList: ArrayList<ArticleModel> = ArrayList()
                val jsonArray = JSONArray(it)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    jsonObject.apply {
                        val content = getJSONObject("content").getString("rendered")
                        val senatizedContent =
                            HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
                                .toString()
                        val totalWords = senatizedContent.split(" ").size
                        val readingTime = totalWords / 200
                        val imageUrl = getJSONObject("_embedded").optJSONArray("wp:featuredmedia")
                            ?.getJSONObject(0)?.getJSONObject("media_details")?.getJSONObject("sizes")
                            ?.getJSONObject("full")?.getString("source_url")
                        logInfo(
                            "_Embedded",
                            imageUrl?:"No Image"
                        )
                        val model =
                            ArticleModel(
                                id = getInt("id"),
                                date = getString("date"),
                                title = getJSONObject("title").getString("rendered"),
                                content = content,
                                excerpt = getJSONObject("excerpt").getString("rendered"),
                                author_name = getJSONObject("_embedded").getJSONArray("author")
                                    .getJSONObject(0).getString("name"), // author name
                                author_url = getJSONObject("_embedded").getJSONArray("author")
                                    .getJSONObject(0).getString("link"),
                                author_pic = getJSONObject("_embedded").getJSONArray("author")
                                    .getJSONObject(0).getJSONObject("avatar_urls").getString("96"),
                                reading_time = readingTime.toString(),
                                link = getString("link"),
                                image = imageUrl?:"",
                                categories = getJSONArray("categories").get(0).toString().toInt()
                            )
                        tempList.add(model)
                    }
                }
                if (tempList.isNotEmpty()) {
                    articleLiveData.value = APIResponses.Success(tempList)
                }
            }


        }, {
            articleLiveData.value = APIResponses.Error(it.toString())
        })
        requestQueue.add(stringRequest)
    }
}