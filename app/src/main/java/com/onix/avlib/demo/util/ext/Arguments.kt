@file:Suppress("UNCHECKED_CAST")

package com.onix.avlib.demo.util.ext

import android.app.Activity
import androidx.fragment.app.Fragment


fun <T : Any> Activity.argument(key: String) = lazy { intent.extras!![key] as T }

fun <T : Any> Activity.argument(key: String, defaultValue: T? = null) = lazy {
    if (intent?.extras == null)
        return@lazy defaultValue
    else {
        intent.extras!![key] as? T ?: defaultValue
    }
}

fun <T : Any> Fragment.argument(key: String) = lazy { arguments?.get(key) as T }

fun <T : Any> Fragment.argument(key: String, defaultValue: T? = null) = lazy {
    arguments?.get(key) as? T ?: defaultValue
}
