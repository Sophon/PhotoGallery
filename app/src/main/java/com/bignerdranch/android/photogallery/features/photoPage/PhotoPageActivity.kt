package com.bignerdranch.android.photogallery.features.photoPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.data.model.GalleryItem

private const val ARG_GALLERY_ITEM = "gallery_item"

class PhotoPageActivity: AppCompatActivity() {

    companion object {
        fun newIntent(context: Context, galleryItem: GalleryItem): Intent {
            return Intent(context, PhotoPageActivity::class.java).apply {
                putExtra(ARG_GALLERY_ITEM, galleryItem)
            }
        }
    }

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_photo_page)

        val fragmentManager = supportFragmentManager
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.photo_page_fragment_container)

        if(currentFragment == null) {
            val fragment =
                PhotoPageFragment.newInstance(
                    intent.extras!!.get(ARG_GALLERY_ITEM) as GalleryItem
                )

            fragmentManager.beginTransaction()
                .add(R.id.photo_page_fragment_container, fragment)
                .commit()
        }
    }
    //endregion
}