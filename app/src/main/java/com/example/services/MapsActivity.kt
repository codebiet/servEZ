package com.example.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getuserlocation()
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

    }


    fun getuserlocation(){
        Toast.makeText(this,"PermissionGranted", Toast.LENGTH_LONG).show()
        Toast.makeText(this,"Loading Real Time Location",Toast.LENGTH_LONG).show()
        var mylocation=MylocationListner()
        var locationmanger= getSystemService(Context.LOCATION_SERVICE) as LocationManager


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationmanger.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,mylocation)
        var myth=myThread()
        myth.start()
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

    var local: Location?=null
    inner class MylocationListner: LocationListener {


        constructor(){
            local= Location("Start")
            local!!.latitude=0.0
            local!!.longitude=0.0

        }


        override fun onLocationChanged(location: Location) {

            local=location

        }

    }
    var oldlocation: Location?=null
    inner class myThread:Thread{
        constructor():super(){
            oldlocation= Location("Start")
            oldlocation!!.latitude=0.0
            oldlocation!!.longitude=0.0
        }

        override fun run() {
            while (true){
                try {

                    if(oldlocation!!.distanceTo(local)==0f){
                        continue
                    }
                    oldlocation=local
                    runOnUiThread(){
                        mMap.clear()

                        var jhansi = LatLng(local!!.latitude, local!!.longitude)
                        mMap!!.addMarker(
                            MarkerOptions()
                                .position(jhansi)
                                .snippet("your location")
                                .title("me")

                                )
                        mMap.moveCamera (CameraUpdateFactory.newLatLngZoom(jhansi, 16f))

                    }
                    Thread.sleep(1000)
                }
                catch (ex: Exception){}
            }
        }
    }

}