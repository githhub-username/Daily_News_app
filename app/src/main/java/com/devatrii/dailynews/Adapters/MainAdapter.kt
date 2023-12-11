package com.devatrii.dailynews.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devatrii.dailynews.ArticleActivity
import com.devatrii.dailynews.Models.ArticleModel
import com.devatrii.dailynews.databinding.ItemNewsOneBinding
import com.devatrii.dailynews.databinding.ItemNewsTwoBinding
import com.devatrii.dailynews.utils.loadImageWithGlide
import com.devatrii.dailynews.utils.setHtmlAsText

const val LAYOUT_CARD = 1
const val LAYOUT_LIST = 2
const val INTENT_ARTICLE_MODEL_EXTRA = "article_model"
class MainAdapter(var list: ArrayList<ArticleModel>, var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CardViewHolder(var binding: ItemNewsOneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ArticleModel, context: Context) {
            binding.apply {
                rvTitle.setHtmlAsText(model.title)
                rvExcerpt.setHtmlAsText(model.excerpt)
                loadImageWithGlide(model.image, rvImageView, context)
                mCardView.setOnClickListener {
                    val intent = Intent().apply {
                        putExtra(INTENT_ARTICLE_MODEL_EXTRA, model)
                        setClass(context, ArticleActivity::class.java)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    inner class ListViewHolder(var binding: ItemNewsTwoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ArticleModel, context: Context) {
            binding.apply {
                rvPostTitle.setHtmlAsText(model.title)
                rvAuthor.text = model.author_name
                rvReadTime.text = "• ${model.reading_time.split(" ")[0]}m Read"
                loadImageWithGlide(model.image, rvImage, context)
                mCardViewPost.setOnClickListener {
                    val intent = Intent().apply {
                        putExtra(INTENT_ARTICLE_MODEL_EXTRA, model)
                        setClass(context, ArticleActivity::class.java)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].LAYOUT_TYPE == LAYOUT_CARD) {
            LAYOUT_CARD
        } else {
            LAYOUT_LIST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LAYOUT_CARD) {
            CardViewHolder(ItemNewsOneBinding.inflate(LayoutInflater.from(context), parent, false))
        } else {
            ListViewHolder(ItemNewsTwoBinding.inflate(LayoutInflater.from(context), parent, false))
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == LAYOUT_CARD) {
            (holder as CardViewHolder).bind(model = list[position], context = context)
        } else {
            (holder as ListViewHolder).bind(model = list[position], context = context)
        }
    }
}