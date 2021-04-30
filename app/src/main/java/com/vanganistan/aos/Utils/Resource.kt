package com.vanganistan.aos.Utils

class Resource<T> private constructor(
    val state: State,
    val data: T?,
    val error: Throwable?
) {

    enum class State {
        LOADING,
        SUCCESS,
        ERROR
    }


    companion object {
        fun <T> loading(): Resource<T> =
            Resource(
                state = State.LOADING,
                data = null,
                error = null
            )

        fun <T> success(data: T): Resource<T> =
            Resource(
                state = State.SUCCESS,
                data = data,
                error = null
            )

        fun <T> error(error: Throwable?, data: T? = null): Resource<T> =
            Resource(
                state = State.ERROR,
                data = data,
                error = error
            )

        fun <T> custom(state: State, data: T?, error: Throwable?): Resource<T> =
            Resource(
                state = state,
                data = data,
                error = error
            )
    }

    override fun toString(): String {
        return "Resource(state=$state, data=$data, error=$error)"
    }
}

fun <T> Resource<T>?.isSuccessful(): Boolean = this?.state == Resource.State.SUCCESS
