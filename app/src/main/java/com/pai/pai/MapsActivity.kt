package com.pai.pai

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pai.pai.databinding.ActivityMapsBinding
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var btnMaps: Button
    private lateinit var geocoder: Geocoder
    private lateinit var coordenadas: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)


        btnMaps = findViewById(R.id.btn_maps)
        btnMaps.setOnClickListener{
            traducirCoordenadas()
        }

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
        activateMyLocation()

        mMap.setOnMapClickListener { coordenada ->

            this.coordenadas = coordenada
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(coordenadas))
            btnMaps.isEnabled = true

        }

        // Add a marker in Sydney and move the camera
        val monterrey = LatLng(25.67, -100.31)
        mMap.addMarker(MarkerOptions().position(monterrey).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(monterrey, 10F))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
    }


    private fun traducirCoordenadas() {

        geocoder = Geocoder(this, Locale.getDefault())

        //Función asíncrona
        Thread {

            val direcciones = geocoder.getFromLocation(coordenadas.latitude, coordenadas.longitude, 1)
            if (direcciones.size > 0) {

                val direccion = direcciones[0].getAddressLine(0)

                setResult(RESULT_OK, Intent().putExtra("ubicacion", direccion))
                finish()
            } else {
                finish()
            }
        }.start()
    }

    @SuppressLint("MissingPermission")
    private fun activateMyLocation(){


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            mMap.isMyLocationEnabled = true
        }
    }
}