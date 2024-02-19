package com.example.todolist.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.todolist.R
import com.example.todolist.TodoListApplication
import com.example.todolist.ui.fragment.TaskEditFragment
import com.example.todolist.util.DrawableComparator.bytesEqualTo
import com.example.todolist.viewmodels.TaskViewModel
import com.example.todolist.viewmodels.TaskViewModelFactory
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    //    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(
            this.application as TodoListApplication, this
        )
    }
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

        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        // set icon for visibility
        savedInstanceState?.getBoolean("isVisible")?.apply {
            bottomAppBar.menu[0].icon = if (this) {
                ResourcesCompat.getDrawable(
                    resources, R.drawable.round_visibility_24, theme
                )
            } else {
                ResourcesCompat.getDrawable(
                    resources, R.drawable.round_visibility_off_24, theme
                )
            }
        }
        // save visibility state
        bottomAppBar.menu[0].icon?.apply {
            when (this.bytesEqualTo(
                ResourcesCompat.getDrawable(resources, R.drawable.round_visibility_24, theme)
            )) {
                true -> {
                    viewModel.saveVisibility(true)
                }

                false -> {
                    viewModel.saveVisibility(false)
                }
            }
        }
        // set click listener for each menu item
        bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.visibility -> {
                    // change icon and show/hide tasks
                    it.icon?.apply {
                        when (this.bytesEqualTo(
                            ResourcesCompat.getDrawable(
                                resources, R.drawable.round_visibility_24, theme
                            )
                        )) {
                            true -> {
                                // show only incomplete tasks
                                viewModel.saveVisibility(false)
                                it.icon = ResourcesCompat.getDrawable(
                                    resources, R.drawable.round_visibility_off_24, theme
                                )
                            }

                            false -> {
                                // show all tasks
                                viewModel.saveVisibility(true)
                                it.icon = ResourcesCompat.getDrawable(
                                    resources, R.drawable.round_visibility_24, theme
                                )
                            }
                        }
                    }
                    true
                }

                else -> false
            }
        }
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        Log.d(null, "onSaveInstanceState")
        findViewById<BottomAppBar>(R.id.bottomAppBar).menu[0].icon?.apply {
            when (this.bytesEqualTo(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.round_visibility_24, theme
                )
            )) {
                true -> {
                    outState.putBoolean("isVisible", true)
                }

                false -> {
                    outState.putBoolean("isVisible", false)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * @see <a href="https://developer.android.com/develop/ui/views/components/dialogs#FullscreenDialog">
     *     Show a dialog full screen/Show a fragment above other fragments</a>
     */
    private fun showDialog() {
        val transaction = supportFragmentManager.beginTransaction()
        // Remove any currently showing dialog
        val previous = supportFragmentManager.findFragmentByTag("dialog")
        if (previous != null) transaction.remove(previous)
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        val newFragment = TaskEditFragment()
        transaction.addToBackStack(null).add(android.R.id.content, newFragment).commit()
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