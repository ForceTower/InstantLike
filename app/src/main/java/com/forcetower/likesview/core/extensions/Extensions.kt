package com.forcetower.likesview.core.extensions

import kotlin.math.floor
import kotlin.math.min
import kotlin.math.pow

fun <T> List<T>.limit(limit: Int): List<T> {
    return this.subList(0, min(limit, size))
}

fun Double.truncate(decimals: Int = 1): Double {
    val power = 10.0.pow(decimals.toDouble())
    return floor(this * power) / power
}

val <T> T.checkAllMatched: T
    get() = this