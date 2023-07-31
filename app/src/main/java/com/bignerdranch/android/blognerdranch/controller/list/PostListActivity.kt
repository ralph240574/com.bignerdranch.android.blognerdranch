package com.bignerdranch.android.blognerdranch.controller.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.blognerdranch.databinding.ActivityPostListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postRecyclerview.layoutManager = LinearLayoutManager(this)

        val listViewModel by viewModels<ListViewModel>()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                listViewModel.uiState.collect { uiState ->
                    when {
                        uiState.postMetadataList.isNotEmpty() -> {
                            binding.postRecyclerview.adapter = PostAdapter(uiState.postMetadataList)
                            binding.circularProgressIndicator.visibility = View.GONE
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

    private fun showToast(text: String) {
        val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast.show()
    }

    companion object {
        const val TAG = "PostListActivity"
    }
}
