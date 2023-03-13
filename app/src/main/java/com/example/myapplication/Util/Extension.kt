package com.example.myapplication

import android.view.View

fun String.takeIfNotEmpty(textValue: (textValue: String) -> Unit) = this
    .takeIf { it.isNotEmpty() }
    ?.also { textValue(it) }

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}
