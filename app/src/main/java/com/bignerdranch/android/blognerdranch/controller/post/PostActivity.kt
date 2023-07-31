package com.bignerdranch.android.blognerdranch.controller.post

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bignerdranch.android.blognerdranch.databinding.ActivityPostBinding
import com.bignerdranch.android.blognerdranch.model.Post
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostActivity : AppCompatActivity() {

    private var postId: Int = 0

    private lateinit var binding: ActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getIntExtra(EXTRA_POST_ID, 0)

        val postViewModel by viewModels<PostViewModel>()
        postViewModel.getPost(postId)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                postViewModel.uiState.collect { uiState ->
                    when {
                        uiState.post != null -> {
                            updateUI(post = uiState.post)
                        }

                        uiState.exception != null -> {
                            showToast(uiState.exception.toString())
                            binding.circularProgressIndicator.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun updateUI(post: Post) {
        binding.titleTextview.text = post.metadata?.title
        binding.authorTextView.text = post.metadata?.author?.name
        binding.bodyTextView.text = post.body
        binding.circularProgressIndicator.visibility = View.GONE
    }

    private fun showToast(text: String) {
        val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast.show()
    }

    companion object {
//        const val TAG = "PostActivity"

        const val EXTRA_POST_ID = "postID"

        fun newIntent(context: Context, id: Int): Intent {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra(EXTRA_POST_ID, id)
            return intent
        }
    }
}
