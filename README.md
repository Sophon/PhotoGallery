# PhotoGallery

A simple Flickr viewing client.

An educational app inspired by the book [Android Programming: The Big Nerd Ranch Guide (4th Edition)](https://www.amazon.com/Android-Programming-Ranch-Guide-Guides/dp/0135245125).

This app uses:
- MVVM architecture with fragments, view models, LiveData and view binding
- repository pattern for Room & Retrofit, view-holder pattern for RecyclerView
- Room, Picasso, Retrofit, Gson, Timber
- SearchView, notifications, Workers and SharedPreferences
- UI with overflow menu and bottom navigation panel

<img src="/image/gallery.gif" width="270" height="555">

TODO:

☐ tests - both unit tests and UI tests

☐ authentication - right now you must create a file named `apikey.properties` and inside provide `FLICKR_API_KEY="your_flickr_api_key`

☐ options - mainly to customize notification behavior

☐ other Flickr functionalities
