package com.decagonhq.clads

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.decagonhq.clads.ui.BaseFragment
import com.decagonhq.clads.ui.profile.DashboardActivity
import com.decagonhq.clads.util.Constants.LOCATION_REQUEST_CODE
import com.decagonhq.clads.util.LocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit




@AndroidEntryPoint
class MapsFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var geocoder: Geocoder
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationRequest = LocationRequest()
        // initialize fused Location Client to hep in getting location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    fun initializeLocations() {
        getLocationCoOrdinates()
        geocoder = Geocoder(context, Locale.getDefault())
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }


    private fun getLocationCoOrdinates() {

        locationRequest.apply {
            interval = TimeUnit.SECONDS.toMillis(7000)
            fastestInterval = TimeUnit.SECONDS.toMillis(5000)
            maxWaitTime = TimeUnit.SECONDS.toMillis(30000)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        /* callback for location result after request */
        locationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {

                if (locationResult.locations.isNullOrEmpty()) {
                    showToast("No Location")
                } else {
                    val location = locationResult.locations

                    Log.d("AAAAA", "$location")

                    Toast.makeText(requireContext(), location.toString(), Toast.LENGTH_LONG ).show()
                    latitude = location[0].latitude
                    longitude = location[0].longitude

                }
            }
        }
    }


    private fun getLocationPermission() {
        if (LocationPermission.checkPermission(requireActivity() as DashboardActivity)) {
            // if permission is granted, get the latest updates
            showToast("Permission granted")
            initializeLocations()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // if permission is granted after being denied at first, get update
        if (grantResults.contains(PackageManager.PERMISSION_GRANTED) && requestCode == LOCATION_REQUEST_CODE) {
            initializeLocations()
            showToast("Permission granted")
        } else {
            showToast("Location permission is required for this feature to run")
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        if(LocationPermission.checkPermission(requireActivity() as DashboardActivity)){
            googleMap.isMyLocationEnabled = true

            val artisanLocation = LatLng(latitude, longitude)

            Log.d("DDDDDD", "$artisanLocation")

            googleMap.setOnMapClickListener {
                try {
                    val address = geocoder.getFromLocation(
                        it.latitude,
                        it.longitude,
                        1
                    )[0]

                    val loc = "${address.featureName} ${address.thoroughfare}, ${address.subAdminArea}"

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(it)
                            .draggable(true)
                            .title("Marker in $loc")
                    ).showInfoWindow()

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            googleMap.setOnMapLongClickListener {
                googleMap.clear()

                try {
                    val address = geocoder.getFromLocation(
                        it.latitude,
                        it.longitude,
                        1
                    )[0]

                    val loc = "${address.featureName} ${address.thoroughfare}, ${address.subAdminArea}"

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(it)
                            .draggable(true)
                            .title("Marker in $loc")
                    ).showInfoWindow()

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            googleMap.addMarker(
                MarkerOptions()
                    .position(artisanLocation)
                    .title("Marker in $artisanLocation")
                    .draggable(true)
            ).showInfoWindow()

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(artisanLocation))

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(artisanLocation, 15f))
            // googleMap.setEnableMyLocation(true)


            googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {

                override fun onMarkerDragStart(marker: Marker) {}
                override fun onMarkerDrag(marker: Marker) {}
                override fun onMarkerDragEnd(marker: Marker) {

                    val latLng = marker.position

                    try {
                        val address = geocoder.getFromLocation(
                            latLng.latitude,
                            latLng.longitude,
                            1
                        )[0]

                        marker.title =
                            "${address.featureName} ${address.thoroughfare}, ${address.subAdminArea}"
                        marker.showInfoWindow()

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })



            var mapView = View(requireContext())
            if (mapView.findViewById<View>("1".toInt()) != null
            ) {
                // Get the button view
                val locationButton =
                    (mapView.findViewById<View>("1".toInt())
                        .parent as View).findViewById<View>("2".toInt())
                // and next place it, on bottom right (as Google Maps app)
                val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
                // position on right bottom
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE)
                layoutParams.setMargins(0, 0, 30, 30)


                locationButton.setOnClickListener {
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            artisanLocation.latitude,
                            artisanLocation.longitude
                        ), 16f
                    )
                    googleMap.animateCamera(cameraUpdate, 250, null)
                }
            }
        } else {
            getLocationPermission()
        }
    }

}