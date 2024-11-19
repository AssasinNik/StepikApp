package com.nikitacherenkov.stepikapp.core.data.networking

import com.nikitacherenkov.stepikapp.core.domain.util.NetworkError
import retrofit2.Response
import com.nikitacherenkov.stepikapp.core.domain.util.Result

suspend inline fun<reified T> responseToResult(
    response: Response<T>
): Result<T, NetworkError>{
    return when{
        response.isSuccessful -> {
            val body = response.body()
            if (body != null){
                Result.Success(body)
            } else{
                Result.Error(NetworkError.SERIALIZATION_ERROR)
            }
        }
        response.code() == 408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        response.code() == 429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
        response.code() in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}