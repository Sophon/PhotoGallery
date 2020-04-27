package com.bignerdranch.android.photogallery.view.gallery

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.databinding.FragmentPhotoGalleryBinding
import com.bignerdranch.android.photogallery.viewModel.gallery.PhotoGalleryViewModel

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment: Fragment() {
    //region Private vars
    private lateinit var fragmentBinding: FragmentPhotoGalleryBinding
    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel
    //endregion

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoGalleryViewModel =
            ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_photo_gallery, menu)

        val searchView: SearchView =
            menu.findItem(R.id.gallery_menu_search).actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(queryText: String): Boolean {
                    Log.d(TAG, "onQueryTextSubmit: $queryText")

                    photoGalleryViewModel.searchPhotos(queryText)

                    hideKeyboard()

                    return true
                }

                override fun onQueryTextChange(queryText: String): Boolean {
                    Log.d(TAG, "onQueryTextChange: $queryText")

                    photoGalleryViewModel.searchPhotos(queryText)

                    return false
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)

        fragmentBinding.galleryRecyclerView.layoutManager = GridLayoutManager(context, 3)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                fragmentBinding.galleryRecyclerView.adapter = PhotoAdapter(galleryItems)
            }
        )
    }

    //endregion

    //region Extension funs
    private fun View.hideKeyboard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
    //endregion
}