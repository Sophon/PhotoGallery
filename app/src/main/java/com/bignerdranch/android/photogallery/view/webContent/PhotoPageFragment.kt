package com.bignerdranch.android.photogallery.view.webContent

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.photogallery.databinding.FragmentPhotoPageBinding
import com.bignerdranch.android.photogallery.view.VisibleFragment

private const val ARG_URI = "photo_page_uri"

class PhotoPageFragment: VisibleFragment() {

    //region Private vars
    private lateinit var fragmentBinding: FragmentPhotoPageBinding
    private lateinit var uri: Uri
    //endregion

    companion object {
        fun newInstance(uri: Uri): PhotoPageFragment {
            return PhotoPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_URI, uri)
                }
            }
        }
    }

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uri = arguments?.getParcelable(ARG_URI) ?: Uri.EMPTY
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
            loadUrl(uri.toString())
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
}