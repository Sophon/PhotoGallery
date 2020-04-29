package com.bignerdranch.android.photogallery

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.photogallery.databinding.ActivityPhotoGalleryBinding
import com.bignerdranch.android.photogallery.view.gallery.PhotoGalleryFragment

private lateinit var binding: ActivityPhotoGalleryBinding

class PhotoGalleryActivity : AppCompatActivity() {

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

        val isFragmentContainerEmpty = (savedInstanceState == null)
        if(isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, PhotoGalleryFragment())
                .commit()
        }
    }
    //endregion
}
