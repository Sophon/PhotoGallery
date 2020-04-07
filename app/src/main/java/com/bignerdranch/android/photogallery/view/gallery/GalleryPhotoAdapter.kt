package com.bignerdranch.android.photogallery.view.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.photogallery.databinding.ListItemGalleryBinding
import com.bignerdranch.android.photogallery.model.GalleryItem

internal class PhotoAdapter(private val galleryItems: List<GalleryItem>)
    : RecyclerView.Adapter<PhotoHolder>() {
    //region Private vars
    private lateinit var binding: ListItemGalleryBinding
    //endregion

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        binding = ListItemGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PhotoHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val galleryItem: GalleryItem = galleryItems[position]

        holder.bind(galleryItem)
    }

    override fun getItemCount(): Int = galleryItems.count()
}