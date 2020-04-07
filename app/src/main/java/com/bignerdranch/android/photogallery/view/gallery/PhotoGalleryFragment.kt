package com.bignerdranch.android.photogallery.view.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.photogallery.databinding.FragmentPhotoGalleryBinding

class PhotoGalleryFragment: Fragment() {
    //region Private vars
    private lateinit var binding: FragmentPhotoGalleryBinding
    //endregion

    //region Lifecycle
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)

        binding.galleryRecyclerView.layoutManager = GridLayoutManager(context, 3)

        return binding.root
    }
    //endregion
}