package com.example.memej.Utils

data class ResourcesUtil<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): ResourcesUtil<T> {
            return ResourcesUtil(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): ResourcesUtil<T> {
            return ResourcesUtil(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): ResourcesUtil<T> {
            return ResourcesUtil(Status.LOADING, data, null)
        }
    }
}