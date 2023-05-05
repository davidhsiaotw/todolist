package com.example.todolist

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.network.QuoteApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    //    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val float = findViewById<FloatingActionButton>(R.id.add_task)
        float.setOnClickListener {
            showDialog()
        }
        findViewById<BottomAppBar>(R.id.bottomAppBar).setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_quote -> {
                    // TODO: show daily quote
                    val call = QuoteApi.retrofitService.getQuote(50, 10)
                    call.enqueue(object : Callback<List<Map<String, Any>>> {
                        override fun onResponse(
                            call: Call<List<Map<String, Any>>>,
                            response: Response<List<Map<String, Any>>>
                        ) {
                            val body = response.body()?.get(0)?.get("content")
                            if (body != null) {
                                MaterialAlertDialogBuilder(this@MainActivity)
                                    .setTitle("Daily Quote")
                                    .setMessage(body as String)
                                    .setPositiveButton("REGENERATE") { dialog, _ ->
                                        // TODO: call api again -> close current dialog -> open
                                        //  new dialog
                                        dialog.dismiss()
                                    }.show()
                            }
                        }

                        override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                            MaterialAlertDialogBuilder(this@MainActivity)
                                .setMessage(t.stackTraceToString()).show()
                        }

                    })

                    true
                }
                else -> false
            }
        }
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * @see <a href="https://developer.android.com/develop/ui/views/components/dialogs#FullscreenDialog">reference</a>
     */
    fun showDialog() {
        val fragmentManager = supportFragmentManager
        val newFragment = SecondFragment()

        val transaction = fragmentManager.beginTransaction()
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()
    }

    // reference: https://www.youtube.com/watch?v=mwzKYIB9cQs&ab_channel=TechProjects
//    fun getCurrentLocation(): String {
//        if (checkPermissions()) {
//            return if (isLocationEnabled()) {
//                if (ActivityCompat.checkSelfPermission(
//                        this, Manifest.permission.ACCESS_FINE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                        this, Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    requestPermission()
//                    return ""
//                }
//
//                val currentLocation =
//                    fusedLocationProviderClient.lastLocation.addOnCompleteListener {
//                        val location = it.result
//                        if (location == null)
//                            Toast.makeText(this, "NULL location", Toast.LENGTH_SHORT).show()
//                    }.result
//
//                "${currentLocation.latitude}, ${currentLocation.longitude}"
//            } else {
//                // open Settings for user
//                Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                ""
//            }
//        } else {
//            requestPermission()
//            return ""
//        }
//    }
//
//    private fun checkPermissions(): Boolean {
//        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED)
//    }
//
//    private fun isLocationEnabled(): Boolean {
//        val locationManager: LocationManager =
//            getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager
//            .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//    }
//
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            this, arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
//            ), 0
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 0) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
//                getCurrentLocation()
//            } else {
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//    var location: String? = null
//    var resultLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) {
//        // XXX: issue: cannot get location from MapsActivity
//        if (it.resultCode == 200) {
//            location = it.data?.getStringExtra("location")
//        }
//    }
//
//    private fun startIntent() {
//        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//        startActivity(intent)
//    }
}