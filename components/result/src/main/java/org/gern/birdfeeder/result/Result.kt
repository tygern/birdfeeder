package org.gern.birdfeeder.result

sealed class Result<T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Failure<T>(val reason: String) : Result<T>()
}
