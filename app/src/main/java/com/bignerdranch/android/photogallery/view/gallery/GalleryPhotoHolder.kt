package com.bignerdranch.android.photogallery.view.gallery

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.photogallery.databinding.ListItemGalleryBinding

internal class PhotoHolder(binding: ListItemGalleryBinding)
    : RecyclerView.ViewHolder(binding.root) {

    private val imageView:AppCompatImageView = binding.galleryItemImagView

    fun bind(drawable: Drawable) {
        imageView.setImageDrawable(drawable)
    }
}