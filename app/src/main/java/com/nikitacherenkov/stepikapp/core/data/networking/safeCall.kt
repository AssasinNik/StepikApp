package com.nikitacherenkov.stepikapp.core.data.networking

import com.nikitacherenkov.stepikapp.core.domain.util.NetworkError
import com.nikitacherenkov.stepikapp.core.domain.util.Result
import kotlinx.coroutines.ensureActive
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> Response<T>
): Result<T, NetworkError> {
    val response = try{
        execute()
    } catch (e: IOException){
        return Result.Error(NetworkError.NO_INTERNET)
    } catch (e: IllegalStateException){
        return Result.Error(NetworkError.SERIALIZATION_ERROR)
    } catch (e: Exception){
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}