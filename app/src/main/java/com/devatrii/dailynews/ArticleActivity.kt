package com.devatrii.dailynews

import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.devatrii.dailynews.Adapters.INTENT_ARTICLE_MODEL_EXTRA
import com.devatrii.dailynews.Models.ArticleModel
import com.devatrii.dailynews.databinding.ActivityArticleBinding
import com.devatrii.dailynews.utils.HTMLImageGetter
import com.devatrii.dailynews.utils.ImageGetter
import com.devatrii.dailynews.utils.convertDateFormat
import com.devatrii.dailynews.utils.loadImageWithGlide
import com.devatrii.dailynews.utils.logDebug
import com.devatrii.dailynews.utils.openChromeTab
import com.devatrii.dailynews.utils.setHtmlAsText
import com.zzhoujay.richtext.LinkHolder
import com.zzhoujay.richtext.RichText
import com.zzhoujay.richtext.callback.LinkFixCallback

private const val TAG = "Article_Activity"

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    val activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val model = intent.getSerializableExtra(INTENT_ARTICLE_MODEL_EXTRA) as ArticleModel
        binding.apply {
            mTitle.setHtmlAsText(model.title)
            mAuthorName.text = model.author_name
            loadImageWithGlide(model.author_pic,mAuthorImage,activity)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mPublishDate.text = convertDateFormat(model.date)
            }
            mFollow.setOnClickListener {
                openChromeTab(activity, model.author_url)
            }
//            mArticle.displayHtml(model.content)
            val imageGetter = HTMLImageGetter(resources, mArticle, activity)
            val richText = RichText.fromHtml(model.content)
                .imageGetter(imageGetter)
            richText.autoFix(true)
            richText.linkFix(object : LinkFixCallback {
                override fun fix(holder: LinkHolder?) {
                    logDebug(TAG, "Links Fixed ${holder!!.url}")
                }
            })
            richText.urlClick { url ->
                openChromeTab(activity, url)
                logDebug(TAG, "Clicked URL $url")
                true
            }
            richText.into(mArticle)
            loadImageWithGlide(model.image, mArticleBanner, activity)
        }
    }

    private fun TextView.displayHtml(html: String) {
        val imageGetter = ImageGetter(resources, this, activity)

        val styledText = HtmlCompat.fromHtml(
            html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null
        )
        movementMethod = LinkMovementMethod.getInstance()
        text = styledText


    }


}