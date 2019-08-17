package com.forcetower.likesview.core.extensions

import kotlin.math.min

fun <T> List<T>.limit(limit: Int): List<T> {
    return this.subList(0, min(limit, size))
}