package com.bignerdranch.android.photogallery

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bignerdranch.android.photogallery.databinding.ActivityPhotoGalleryBinding
import com.bignerdranch.android.photogallery.model.GalleryType
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

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoGalleryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.botNav_web -> {
                    galleryType.value = GalleryType.ONLINE
                }
                else -> {
                    galleryType.value = GalleryType.FAVORITES
                }
            }

            true
        }

        val isFragmentContainerEmpty = (savedInstanceState == null)
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
