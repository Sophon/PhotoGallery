package com.bignerdranch.android.photogallery.view.webContent

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.photogallery.R

class PhotoPageActivity: AppCompatActivity() {

    companion object {
        fun newIntent(context: Context, photoPageUri: Uri): Intent {
            return Intent(context, PhotoPageActivity::class.java).apply {
                data = photoPageUri
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
            val fragment = PhotoPageFragment.newInstance(intent.data!!)

            fragmentManager.beginTransaction()
                .add(R.id.photo_page_fragment_container, fragment)
                .commit()
        }
    }
    //endregion
}