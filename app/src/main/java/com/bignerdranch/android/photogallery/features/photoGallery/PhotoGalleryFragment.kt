package com.bignerdranch.android.photogallery.features.photoGallery

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.databinding.FragmentPhotoGalleryBinding
import com.bignerdranch.android.photogallery.data.model.GalleryType
import com.bignerdranch.android.photogallery.app.GalleryPreferences
import com.bignerdranch.android.photogallery.features.shared.VisibleFragment
import timber.log.Timber

class PhotoGalleryFragment: VisibleFragment() {

    private lateinit var fragmentBinding: FragmentPhotoGalleryBinding
    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel

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

        setupSearch(menu)

        setupPollingMenuButton(menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)

        fragmentBinding.galleryRecyclerView.layoutManager = GridLayoutManager(context, 3)

        fragmentBinding.galleryRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter =
                PhotoAdapter(
                    requireContext()
                )
        }

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoGalleryViewModel.galleryLiveData.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                (fragmentBinding.galleryRecyclerView.adapter as PhotoAdapter).submitList(galleryItems)
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.gallery_menu_toggle_polling -> {
                photoGalleryViewModel.togglePolling()

                activity?.invalidateOptionsMenu()

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //endregion

    //region Setup
    private fun setupSearch(menu: Menu) {
        val searchView: SearchView =
            menu.findItem(R.id.gallery_menu_search).actionView as SearchView

        photoGalleryViewModel.setInitialQuery(searchView)

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(queryText: String): Boolean {
                    photoGalleryViewModel.searchPhotos(queryText)

                    hideKeyboard()

                    clearFocus()

                    return true
                }

                override fun onQueryTextChange(queryText: String): Boolean {
                    photoGalleryViewModel.liveSearchPhotos(queryText)

                    return false
                }
            })
        }
    }

    private fun setupPollingMenuButton(menu: Menu) {
        val pollingButton = menu.findItem(R.id.gallery_menu_toggle_polling)
        pollingButton.setTitle(
            if(GalleryPreferences.isPolling(requireContext())) {
                R.string.stop_polling
            } else {
                R.string.start_polling
            }
        )
    }
    //endregion

    private fun View.hideKeyboard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    public fun switchGalleryTo(galleryType: GalleryType) {
        Timber.d("Gallery: switching galleries")

        photoGalleryViewModel.switchGalleryTo(galleryType)
    }
}