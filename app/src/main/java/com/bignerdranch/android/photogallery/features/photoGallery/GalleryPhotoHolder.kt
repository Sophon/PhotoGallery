package com.bignerdranch.android.photogallery.features.photoGallery

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.photogallery.databinding.ListItemGalleryBinding
import com.bignerdranch.android.photogallery.data.model.GalleryItem
import com.bignerdranch.android.photogallery.features.photoPage.PhotoPageActivity
import com.squareup.picasso.Picasso
import timber.log.Timber

internal class PhotoHolder(
    private val context: Context,
    binding: ListItemGalleryBinding
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    //region Vars
    private val imageView: AppCompatImageView = binding.galleryItemImageView
    private lateinit var galleryItem: GalleryItem
    //endregion

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Timber.d("Photo: clicked on a photo -> ${galleryItem.photoPageUri}")

        val intent = PhotoPageActivity.newIntent(context, galleryItem)
        context.startActivity(intent)
    }

    fun bind(galleryItem: GalleryItem) {
        this.galleryItem = galleryItem

        Picasso.get()
            .load(galleryItem.url)
            .into(imageView)
    }
}