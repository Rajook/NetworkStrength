package com.example.networkstrength.utilities

import android.graphics.Color
import android.view.View
import android.widget.Button


fun View.enable(enabled: Boolean) {
    isEnabled = enabled
//    alpha = if (enabled) 1f else 0.5f
    alpha = if (enabled) 1f else 1f
}

fun Button.loadButton(company: Int) {
    if (company == 0){
        this.setBackgroundColor(Color.BLACK)
        this.setTextColor(Color.WHITE)
    }else{
        this.setBackgroundColor(Color.BLUE)
        this.setTextColor(Color.WHITE)
    }
}