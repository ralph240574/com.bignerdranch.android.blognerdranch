package com.bignerdranch.android.blognerdranch.controller.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.blognerdranch.data.Result
import com.bignerdranch.android.blognerdranch.data.remote.BlogRemoteDataSource
import com.bignerdranch.android.blognerdranch.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UiState(
    val loading: Boolean = false,
    val post: Post? = null,
    val exception: Throwable? = null
)

@HiltViewModel
class PostViewModel @Inject constructor(
    private val remoteDataSource: BlogRemoteDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(loading = true))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getPost(id: Int) {
        viewModelScope.launch {
            remoteDataSource.getPost(id).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update {
                        it.copy(
                            loading = true,
                            exception = null,
                            post = null,
                        )
                    }

                    is Result.Error -> _uiState.update {
                        it.copy(
                            loading = false,
                            exception = result.exception,
                            post = null,
                        )
                    }

                    is Result.Success -> _uiState.update {
                        it.copy(
                            loading = false,
                            exception = null,
                            post = result.data,
                        )
                    }
                }
            }
        }
    }

//    fun getSchool(dbn: String) {
//        dbn::class
//        viewModelScope.launch {
//            re.getSchool(dbn = dbn)
//                .collect { result ->
//                    when (result) {
//                        is Result.Loading -> _uiState.update {
//                            it.copy(
//                                loading = true,
//                                message = null,
//                                exception = null,
//                            )
//                        }
//
//                        is Result.Success -> _uiState.update {
//                            it.copy(
//                                loading = false,
//                                school = result.data,
//                                message = "success",
//                                exception = null,
//                            )
//                        }
//
//                        is Result.Error -> {
//                            _uiState.update {
//                                it.copy(
//                                    loading = false,
//                                    message = "error",
//                                    exception = result.exception,
//                                )
//                            }
//                        }
//                    }
//                }
//        }
//    }
}