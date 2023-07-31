package com.bignerdranch.android.blognerdranch.data.remote


import com.bignerdranch.android.blognerdranch.data.Result
import java.io.IOException

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>): Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        Result.Error(IOException(e.message, e))
    }
}
