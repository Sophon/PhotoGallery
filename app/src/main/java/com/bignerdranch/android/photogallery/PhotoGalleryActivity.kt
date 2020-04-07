package com.bignerdranch.android.photogallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.photogallery.databinding.ActivityPhotoGalleryBinding

private lateinit var binding: ActivityPhotoGalleryBinding

class PhotoGalleryActivity : AppCompatActivity() {

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoGalleryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
    //endregion
}
