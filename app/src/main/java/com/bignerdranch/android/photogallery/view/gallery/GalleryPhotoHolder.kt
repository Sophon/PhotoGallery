package com.bignerdranch.android.photogallery.view.gallery

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.photogallery.databinding.ListItemGalleryBinding
import com.bignerdranch.android.photogallery.model.GalleryItem
import com.squareup.picasso.Picasso

internal class PhotoHolder(binding: ListItemGalleryBinding)
    : RecyclerView.ViewHolder(binding.root) {

    private val imageView:AppCompatImageView = binding.galleryItemImagView

    fun bind(galleryItem: GalleryItem) {
        Picasso.get()
            .load(galleryItem.url)
            .into(imageView)
    }
}