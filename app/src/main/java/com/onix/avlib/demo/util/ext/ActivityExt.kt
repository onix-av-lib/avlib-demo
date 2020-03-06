package com.onix.avlib.demo.util.ext

import android.app.Activity
import android.widget.Toast

fun Activity.toast(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}
