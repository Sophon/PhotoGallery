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

    private val bottomNavItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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

//        val isFragmentContainerEmpty = (savedInstanceState == null)
//        if(isFragmentContainerEmpty) {
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.fragmentContainer, PhotoGalleryFragment(galleryType))
//                .commit()
//        }

        galleryType.observe(
            this,
            Observer { galleryType ->
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer, PhotoGalleryFragment(galleryType))
                    .commit()
            }
        )
    }
    //endregion
}
