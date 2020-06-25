package com.bignerdranch.android.photogallery.view.webContent

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.databinding.FragmentPhotoPageBinding
import com.bignerdranch.android.photogallery.model.GalleryItem
import com.bignerdranch.android.photogallery.view.VisibleFragment

private const val ARG_GALLERY_ITEM = "gallery_item"

class PhotoPageFragment: VisibleFragment() {

    //region Private vars
    private lateinit var fragmentBinding: FragmentPhotoPageBinding
    private lateinit var photoPageViewModel: PhotoPageViewModel
    private lateinit var galleryItem: GalleryItem
    //endregion

    companion object {
        fun newInstance(galleryItem: GalleryItem): PhotoPageFragment {
            return PhotoPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_GALLERY_ITEM, galleryItem)
                }
            }
        }
    }

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoPageViewModel =
            ViewModelProvider(this).get(PhotoPageViewModel::class.java)

        galleryItem = arguments?.getParcelable(ARG_GALLERY_ITEM)!!

        setHasOptionsMenu(true)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding =
            FragmentPhotoPageBinding.inflate(inflater, container, false)

        fragmentBinding.progressBar.max = 100

        fragmentBinding.webView.apply {
            loadUrl(galleryItem.photoPageUri.toString())
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()

            webChromeClient = object: WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if(newProgress == 100) {
                        fragmentBinding.progressBar.visibility = View.GONE
                    } else {
                        fragmentBinding.progressBar.apply {
                            visibility = View.VISIBLE
                            progress = newProgress
                        }
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    (activity as AppCompatActivity).supportActionBar?.subtitle = title
                }
            }
        }

        return fragmentBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_photo_page, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.page_menu_open_in_browser -> {
                openInBrowser(galleryItem.photoPageUri)

                return true
            }

            R.id.save_for_offline -> {
                val responseMsg = if(fragmentBinding.progressBar.visibility == View.GONE) {
                    photoPageViewModel.savePhoto(galleryItem)
                    getString(R.string.screen_prompt_saved)
                } else {
                    getString(R.string.screen_prompt_wait)
                }

                Toast.makeText(
                    requireContext(),
                    responseMsg,
                    Toast.LENGTH_SHORT
                ).show()

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    //endregion

    //region Private funs
    private fun openInBrowser(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
    //endregion
}