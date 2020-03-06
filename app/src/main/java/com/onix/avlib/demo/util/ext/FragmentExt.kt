package com.onix.avlib.demo.util.ext

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onix.avlib.demo.R

fun Fragment.alert(resId: Int) {
    if (activity != null) {
        MaterialAlertDialogBuilder(activity).apply {
            setMessage(resId)
            setCancelable(false)
            setPositiveButton(R.string.ok, null)
        }.show()
    }
}

fun Fragment.toast(resId: Int) {
    if (activity != null)
        Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
}
