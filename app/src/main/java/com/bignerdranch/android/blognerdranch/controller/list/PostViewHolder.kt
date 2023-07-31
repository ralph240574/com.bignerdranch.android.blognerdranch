package com.bignerdranch.android.blognerdranch.controller.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.blognerdranch.R
import com.bignerdranch.android.blognerdranch.controller.post.PostActivity
import com.bignerdranch.android.blognerdranch.di.AppModule.baseUrl
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var postMetadata: PostMetadata? = null

    private val titleTextView: TextView = itemView.findViewById(R.id.title_textview)
    private val authorTextView: TextView = itemView.findViewById(R.id.author_textView)
    private val bodyTextView: TextView = itemView.findViewById(R.id.body_textView)
    private val publishDateTextView: TextView = itemView.findViewById(R.id.publishDate_textView)
    private val imageView: ImageView = itemView.findViewById(R.id.item_image)

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(postMetadata: PostMetadata) {
        this.postMetadata = postMetadata
        titleTextView.text = postMetadata.title ?: ""
        authorTextView.text = postMetadata.author?.name ?: ""
        bodyTextView.text = postMetadata.summary ?: ""

        val imageUrl = baseUrl + postMetadata.author?.imageUrl
        Glide.with(imageView).load(imageUrl).into(imageView)


        try {
            publishDateTextView.text =
                formatDate(postMetadata.publishDate)
        } catch (_: Exception) {
            publishDateTextView.text = postMetadata.publishDate
        }
    }

    private fun formatDate(dateTime: String?): String {
        if (dateTime == null) {
            return ""
        }
        val dateInstant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(dateTime))
        val date = LocalDateTime.ofInstant(dateInstant, ZoneId.of(ZoneOffset.UTC.id))
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    override fun onClick(v: View) {
        val context = v.context
        context.startActivity(PostActivity.newIntent(v.context, postMetadata!!.postId!!))
    }

}