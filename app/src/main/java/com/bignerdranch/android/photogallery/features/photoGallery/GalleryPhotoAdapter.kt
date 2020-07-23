package com.bignerdranch.android.photogallery.features.photoGallery

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bignerdranch.android.photogallery.databinding.ListItemGalleryBinding
import com.bignerdranch.android.photogallery.data.model.GalleryItem

internal class PhotoAdapter(private val context: Context)
    : ListAdapter<GalleryItem, PhotoHolder>(
    DiffCallback()
) {

    private lateinit var binding: ListItemGalleryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        binding = ListItemGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PhotoHolder(
            context,
            binding
        )
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

internal class DiffCallback: DiffUtil.ItemCallback<GalleryItem>() {

    override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}