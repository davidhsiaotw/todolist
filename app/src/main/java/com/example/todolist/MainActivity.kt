package com.example.todolist

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.Manifest
import android.app.Activity
import android.location.Location
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    lateinit var thisView: View
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


    fun permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 0
            )
        } else startIntent()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startIntent()
            } else {
                val snackBar = Snackbar.make(thisView, "無定位功能無法執行程序", Snackbar.LENGTH_INDEFINITE)
                snackBar.setAction("OK") {
                    snackBar.setText("aaaaaaaa")
                    //                        snackBar.dismiss()
                }.setActionTextColor(Color.LTGRAY).show()
            }
        }
    }

    private var location: String? = null
    var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // XXX: issue: cannot get location from MapsActivity
        if (it.resultCode == 200) {
            location = it.data?.getStringExtra("location")
        }
    }

    fun startIntent() {
        val intent = Intent(this, MapsActivity::class.java)
        resultLauncher.launch(intent)
    }
}