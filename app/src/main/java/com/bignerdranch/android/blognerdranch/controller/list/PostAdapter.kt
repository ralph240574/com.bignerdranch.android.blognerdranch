package com.bignerdranch.android.blognerdranch.controller.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.blognerdranch.R
import com.bignerdranch.android.blognerdranch.model.PostMetadata

class PostAdapter(private var postMetadata: List<PostMetadata>) :
    RecyclerView.Adapter<PostViewHolder>() {

    override fun getItemCount(): Int {
        return postMetadata.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_post_list_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postMetadata[position]
        holder.bind(post)
    }

}