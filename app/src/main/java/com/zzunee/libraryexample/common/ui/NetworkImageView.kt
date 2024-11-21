package com.zzunee.libraryexample.common.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.zzunee.libraryexample.R

class NetworkImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {
    fun setImageUrl(url: String?) {
        if(url.isNullOrEmpty()) {
            setImageResource(R.drawable.ic_image_loading)
        } else {
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_broken)
                .into(this)
        }
    }
}
