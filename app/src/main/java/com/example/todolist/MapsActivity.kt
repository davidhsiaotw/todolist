package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * @see <a href="https://github.com/Aria-Lee/30Day_Challenge/blob/master/DAY07_MyLocation(GoogleMap)/app/src/main/java/com/example/fish/day7_mylocationgooglemap/MapsActivity.kt">Code Resource</a>
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
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
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        locationManager()
        mMap.isMyLocationEnabled = true
    }

    var oriLocation: Location? = null

    private fun locationManager() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        var isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        var isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!(isGPSEnabled || isNetworkEnabled))
            Snackbar.make(findViewById(android.R.id.content), "目前無開啟任何定位功能", Snackbar.LENGTH_LONG).show()
        else
            try {
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0L, 0f, locationListener
                    )
                    oriLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } else if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0L, 0f, locationListener
                    )
                    oriLocation =
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
            } catch (ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available")
            }
        if (oriLocation != null) {
            // XXX: issue: oriLocation is null
            val resultIntent = Intent()
            val latitude = oriLocation?.latitude
            val longitude = oriLocation?.longitude
            resultIntent.putExtra("location", "$latitude, $longitude")
            setResult(200, resultIntent)
            finish()

            drawMarker()
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(oriLocation!!.latitude, oriLocation!!.longitude), 12.0f
                )
            )
        }
    }

    private val locationListener = LocationListener { location ->
        if (oriLocation == null) {
            oriLocation = location
            drawMarker()
        }
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude, location.longitude), 12.0f
            )
        )
    }

//        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//        }
//
//        override fun onProviderEnabled(provider: String?) {
//        }
//
//        override fun onProviderDisabled(provider: String?) {
//        }

    private fun drawMarker() {
        var lntLng = LatLng(oriLocation!!.latitude, oriLocation!!.longitude)
        mMap.addMarker(MarkerOptions().position(lntLng).title("Current Position"))
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
