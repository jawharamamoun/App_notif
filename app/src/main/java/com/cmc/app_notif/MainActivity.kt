package com.cmc.app_notif

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var btnLocation: Button
    private lateinit var txtLocation: TextView


    private val LOCATION_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)


        btnLocation = findViewById(R.id.btnLocation)
        txtLocation = findViewById(R.id.txtLocation)


        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        btnLocation.setOnClickListener {
            checkLocationPermission()
        }
    }


    private fun checkLocationPermission() {

        val fineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val coarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (fineLocation == PackageManager.PERMISSION_GRANTED ||
            coarseLocation == PackageManager.PERMISSION_GRANTED
        ) {

            getCurrentLocation()

        } else {


            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_CODE
            )
        }
    }


    private fun getCurrentLocation() {

        val hasFinePermission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val hasCoarsePermission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (!hasFinePermission && !hasCoarsePermission) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                if (location != null) {

                    val latitude = location.latitude
                    val longitude = location.longitude

                    txtLocation.text =
                        "Latitude : $latitude\nLongitude : $longitude"

                } else {

                    txtLocation.text =
                        "Localisation indisponible"

                }
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erreur lors de la récupération",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (requestCode == LOCATION_PERMISSION_CODE) {

            if (
                grantResults.any {
                    it == PackageManager.PERMISSION_GRANTED
                }
            ) {


                getCurrentLocation()

            } else {

                Toast.makeText(
                    this,
                    "Permission de localisation refusée",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }
}