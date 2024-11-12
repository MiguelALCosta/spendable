package com.app.spendable.utils

inline fun <reified T : Enum<T>> String.toEnum(): T? {
    return try {
        enumValueOf<T>(this)
    } catch (_: Throwable) {
        null
    }
}