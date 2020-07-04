package com.bignerdranch.android.photogallery.view.gallery

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.*
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.databinding.FragmentPhotoGalleryBinding
import com.bignerdranch.android.photogallery.model.GalleryItem
import com.bignerdranch.android.photogallery.sharedPreferences.GalleryPreferences
import com.bignerdranch.android.photogallery.view.VisibleFragment
import com.bignerdranch.android.photogallery.viewModel.gallery.PhotoGalleryViewModel
import com.bignerdranch.android.photogallery.workers.PollPhotosWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val POLL_WORK = "POLL_WORK"

class PhotoGalleryFragment: VisibleFragment() {

    //region Private vars
    private lateinit var fragmentBinding: FragmentPhotoGalleryBinding
    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel
    private var viewingOnline = true
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

        photoGalleryViewModel.setInitialQuery(searchView)

        searchView.apply {
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
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

        setupPollingMenuButton(menu)
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

        observeData(photoGalleryViewModel.onlineGalleryLiveData)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.gallery_menu_toggle_polling -> {
                if(GalleryPreferences.isPolling(requireContext())) {
                    GalleryPreferences.setPolling(requireContext(), false)
                    WorkManager.getInstance().cancelUniqueWork(POLL_WORK)
                } else {
                    GalleryPreferences.setPolling(requireContext(), true)
                    createPeriodicPollingWork()
                }

                activity?.invalidateOptionsMenu()

                return true
            }

            R.id.gallery_menu_switch_galleries -> {
                viewingOnline = !viewingOnline

                observeData(
                    if(viewingOnline) {
                        photoGalleryViewModel.onlineGalleryLiveData
                    } else {
                        photoGalleryViewModel.offlineGalleryLiveData
                    }
                )

                activity?.invalidateOptionsMenu()

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //endregion

    //region Private funs
    private fun createPeriodicPollingWork() {
        Timber.d("setting up polling")

        val pollConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val periodicWorkRequest: PeriodicWorkRequest = PeriodicWorkRequest
            .Builder(PollPhotosWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(pollConstraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            POLL_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
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

        val galleryButton = menu.findItem(R.id.gallery_menu_switch_galleries)
        galleryButton.setTitle(
            if(viewingOnline) {
                R.string.offline_gallery
            } else {
                R.string.online_gallery
            }
        )
    }

    private fun observeData(liveDataSource: LiveData<List<GalleryItem>>) {
        liveDataSource.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                fragmentBinding.galleryRecyclerView.adapter =
                    PhotoAdapter(requireContext(), galleryItems)
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