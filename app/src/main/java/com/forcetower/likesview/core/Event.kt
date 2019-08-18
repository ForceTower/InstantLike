package com.forcetower.likesview.core

import androidx.lifecycle.Observer

open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set // Allow external read but not write

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}

class DistinctObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<T> {
    private var current: T? = null
    override fun onChanged(t: T) {
        if (t != current) {
            current = t
            onEventUnhandledContent(t)
        }
    }
}