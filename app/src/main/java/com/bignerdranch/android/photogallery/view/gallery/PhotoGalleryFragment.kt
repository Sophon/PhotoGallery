package com.bignerdranch.android.photogallery.view.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.photogallery.databinding.FragmentPhotoGalleryBinding
import com.bignerdranch.android.photogallery.viewModel.gallery.PhotoGalleryViewModel

class PhotoGalleryFragment: Fragment() {
    //region Private vars
    private lateinit var binding: FragmentPhotoGalleryBinding
    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel
    //endregion

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoGalleryViewModel =
            ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)

        binding.galleryRecyclerView.layoutManager = GridLayoutManager(context, 3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                binding.galleryRecyclerView.adapter = PhotoAdapter(galleryItems)
            }
        )
    }

    //endregion
}