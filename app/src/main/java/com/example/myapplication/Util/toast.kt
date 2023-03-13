package com.example.myapplication.Util

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
        this.let { Toast.makeText(activity, text, duration).show() }