package com.example.andriodtweets

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Button

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        confirm = findViewById(R.id.confirm)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.setOnMapLongClickListener {cordinates->

            googleMap.clear()

            doAsync {


                // Pass a context (e.g. Activity) and locale
                val geocoder = Geocoder(this@MapsActivity)
                val results: List<Address> = geocoder.getFromLocation(

                    cordinates.latitude,
                    cordinates.longitude,
                    5
                )

                val first = results[0]
                val buttonTitle = first.getAddressLine(0)

                runOnUiThread {
                    confirm.text = buttonTitle

                    mMap.addMarker(
                        MarkerOptions().position(cordinates)
                    )
                    updateConfirmButton(first)
                }
            }

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

}
