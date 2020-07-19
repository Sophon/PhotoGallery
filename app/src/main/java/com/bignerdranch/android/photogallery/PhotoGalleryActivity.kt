package com.bignerdranch.android.photogallery

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bignerdranch.android.photogallery.databinding.ActivityPhotoGalleryBinding
import com.bignerdranch.android.photogallery.model.GalleryType
import com.bignerdranch.android.photogallery.sharedPreferences.GalleryPreferences
import com.bignerdranch.android.photogallery.view.gallery.PhotoGalleryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var binding: ActivityPhotoGalleryBinding

class PhotoGalleryActivity : AppCompatActivity() {

    private val galleryType = MutableLiveData<GalleryType>(GalleryType.ONLINE)
    private val galleryFragment = PhotoGalleryFragment()

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, PhotoGalleryActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoGalleryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupBottomNavigation(binding.navigation)

        setupGallery(savedInstanceState == null)
    }

    //region UI setup
    private fun setupBottomNavigation(navigationView: BottomNavigationView) {
        navigationView.setOnNavigationItemSelectedListener { navigationItem ->
            when(navigationItem.itemId) {
                R.id.botNav_online -> {
                    galleryType.value = GalleryType.ONLINE
                }
                else -> {
                    galleryType.value = GalleryType.FAVORITES
                }
            }

            true
        }

        navigationView.selectedItemId =
            when(GalleryPreferences.getGalleryType(this)) {
                GalleryType.ONLINE -> R.id.botNav_online
                else -> R.id.botNav_favorites
            }
    }

    private fun setupGallery(isFragmentContainerEmpty: Boolean) {
        if(isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, galleryFragment)
                .commit()
        }

        galleryType.observe(
            this,
            Observer { galleryType ->
                galleryFragment.switchGalleryTo(galleryType)
            }
        )
    }
    //endregion
}
