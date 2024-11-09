package com.app.spendable.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseInteractor {

    fun <T> makeRequest(request: suspend () -> T, completion: (T) -> Unit): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                request()
            }
            completion(result)
        }
    }
}