package com.example.andriodtweets

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.jetbrains.anko.doAsync
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var confirm: Button
    private var currentAddress: Address? = null
    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var useLocation: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        confirm = findViewById(R.id.confirm)

        locationProvider = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)




        useLocation.setOnClickListener {
            // Request the GPS permission from the user
            // Then, if granted, determine their current location

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                // Permission is already granted
                requestCurrentLocation()
            } else {
                // Permission has not been granted, so we can prompt the user

                // Make sure we're running on Marshmallow or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Prompt the user
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        200
                    )
                }
            }
        }





        confirm.setOnClickListener {
            if (currentAddress != null) {
                val intent = Intent(this, TweetActivity::class.java)
                intent.putExtra("location", currentAddress)
                startActivity(intent)
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapLongClickListener {cordinates->

            googleMap.clear()

           geocodeCoordinates(cordinates)

            //long click functionality
//            val latLng = LatLng(38.898365, -77.046753)
//            val title = "GWU"
//
//            googleMap.clear()
//            googleMap.addMarker(
//                MarkerOptions().position(latLng).title(title)
//            )
//
//            val zoomLevel = 5.0f
//        // You can also use moveCamera if you don't want the animation
//            googleMap.animateCamera(
//                CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel)
//            )

        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Was this the GPS Permission request?
        if (requestCode == 200) {
            // We only request one permission, it's the 1st element
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                requestCurrentLocation()
            } else {
                // Permission was denied
                Toast.makeText(
                    this,
                    "Location permission was denied.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }




    @SuppressLint("MissingPermission")
    fun requestCurrentLocation(){
        locationProvider.requestLocationUpdates(
            LocationRequest.create(),
            locationCallback,
            null
        )

    }

    private val locationCallback= object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)

            //we need single result so shutting off location
            locationProvider.removeLocationUpdates(this)

            val location: Location = result.locations[0]
            val latlng = LatLng(location.latitude, location.longitude)
            geocodeCoordinates(latlng)
        }
    }

    private fun updateConfirmButton(address: Address) {
        // Update the button color -- need to load the color from resources first
        val greenColor = ContextCompat.getColor(
            this, R.color.button_green
        )
        val checkIcon = ContextCompat.getDrawable(
            this, R.drawable.ic_check_black_24dp
        )
        confirm.setBackgroundColor(greenColor)

        // Update the left-aligned icon
        confirm.setCompoundDrawablesWithIntrinsicBounds(checkIcon, null, null, null)

        //Update button text
        confirm.text = address.getAddressLine(0)
        confirm.isEnabled = true
    }


    fun geocodeCoordinates(cordinates: LatLng){

        doAsync {


            // Pass a context (e.g. Activity) and locale
            val geocoder = Geocoder(this@MapsActivity)
            val results: List<Address> = geocoder.getFromLocation(

                cordinates.latitude,
                cordinates.longitude,
                5
            )

            val first: Address = results[0]
            // val buttonTitle = first.getAddressLine(0)

            currentAddress = first

            runOnUiThread {
                // confirm.text = buttonTitle

                mMap.addMarker(
                    MarkerOptions().position(cordinates)
                )
                updateConfirmButton(first)
            }
        }
    }


}
