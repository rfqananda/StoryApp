package com.rifqiananda.storyapp.ui

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.rifqiananda.storyapp.R
import com.rifqiananda.storyapp.databinding.ActivityMapsBinding
import com.rifqiananda.storyapp.helper.Constant
import com.rifqiananda.storyapp.helper.PreferencesHelper
import com.rifqiananda.storyapp.ui.view.StoriesViewModel
import com.rifqiananda.storyapp.ui.view.ViewModelFactory
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var sharedPref: PreferencesHelper

    private val viewModel: StoriesViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        addManyMarker()
    }

    private val boundsBuilder = LatLngBounds.Builder()

    private fun addManyMarker() {

        sharedPref = PreferencesHelper(this)

        val token = sharedPref.getString(Constant.PREF_TOKEN)
        val bearer = "Bearer $token"
        val location = 1

        viewModel.setStoriesMap(bearer, location)
        viewModel.getStoriesMap().observe(this) {

            it.forEach { storyMap ->
                val latLng = LatLng(storyMap.lat, storyMap.lon)
                val addressName = getAddressName(storyMap.lat, storyMap.lon)
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(storyMap.name).snippet(addressName)
                )
                boundsBuilder.include(latLng)
            }

            val bounds: LatLngBounds = boundsBuilder.build()
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    300
                )
            )
        }
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d(TAG, "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}