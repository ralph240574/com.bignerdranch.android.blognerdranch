package com.bignerdranch.android.blognerdranch.controller.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.blognerdranch.data.Result
import com.bignerdranch.android.blognerdranch.data.remote.BlogRemoteDataSource
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * UI state for the Home screen
 */
data class UiState(
    val loading: Boolean = false,
    val postMetadataList: List<PostMetadata> = emptyList(),
    val exception: Throwable? = null
)

@HiltViewModel
class ListViewModel @Inject constructor(
    private val remoteDataSource: BlogRemoteDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(loading = true))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getPostMedaData()
        }
    }

    fun getPostMedaData() {
        viewModelScope.launch {
            remoteDataSource.getPostMetadata().collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update {
                        it.copy(
                            loading = true,
                            exception = null,
                            postMetadataList = emptyList()
                        )
                    }

                    is Result.Error -> _uiState.update {
                        it.copy(
                            loading = false,
                            exception = result.exception,
                            postMetadataList = emptyList(),
                        )
                    }

                    is Result.Success -> _uiState.update {
                        it.copy(
                            loading = false,
                            exception = null,
                            postMetadataList = result.data,
                        )
                    }
                }
            }
        }
    }

}