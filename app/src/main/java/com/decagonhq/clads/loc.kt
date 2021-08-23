package com.decagonhq.clads
//
//package com.decagonhq.clads
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.location.Geocoder
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.app.ActivityCompat
//import com.decagonhq.clads.ui.BaseFragment
//import com.decagonhq.clads.ui.profile.DashboardActivity
//import com.decagonhq.clads.util.Constants.LOCATION_REQUEST_CODE
//import com.decagonhq.clads.util.LocationPermisson
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import dagger.hilt.android.AndroidEntryPoint
//import java.io.IOException
//import java.util.*
//import java.util.concurrent.TimeUnit
//
//@AndroidEntryPoint
//class MapsFragment : BaseFragment() {
//
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private lateinit var locationRequest: LocationRequest
//    private lateinit var locationCallback: LocationCallback
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        getLocationPermission()
//        // initialize fused Location Client to hep in getting location
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_maps, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync(callback)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        stopLocationUpdates()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        getLocationUpdates()
//    }
//
//    private fun getLocationPermission() {
//        if (LocationPermisson.checkPermission(requireActivity() as DashboardActivity)) {
//            // if permission is granted, get the latest updates
//            getLocationUpdates()
////            var googleMap: GoogleMap()
////            googleMap.isMyLocationEnabled = true
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
//
//        } else {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_REQUEST_CODE
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        // if permission is granted after being denied at first, get update
//        if (grantResults.contains(PackageManager.PERMISSION_GRANTED) && requestCode == LOCATION_REQUEST_CODE) {
//            getLocationUpdates()
//        } else {
//            showToast("Location permission is required for this feature to run")
//        }
//    }
//
//    private fun stopLocationUpdates() {
//        fusedLocationClient.removeLocationUpdates(locationCallback)
//    }
//
//    private fun getLocationUpdates() {
//        locationRequest = LocationRequest()
//        locationRequest.interval = 3000
//        locationRequest.fastestInterval = 2000
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//
//        locationCallback = object : LocationCallback() {
//
//            // when call for latest location is ready, commence map update below
//            override fun onLocationResult(locationResult: LocationResult) {
//                if (locationResult.locations.isNotEmpty()) {
//
//                    val location = locationResult.lastLocation
//
//                } else {
//                    Log.d("else", "Error")
//                }
//            }
//        }
//    }
//
//    private val callback = OnMapReadyCallback { googleMap ->
//        /**
//         * Manipulates the map once available.
//         * This callback is triggered when the map is ready to be used.
//         * This is where we can add markers or lines, add listeners or move the camera.
//         * In this case, we just add a marker near Sydney, Australia.
//         * If Google Play services is not installed on the device, the user will be prompted to
//         * install it inside the SupportMapFragment. This method will only be triggered once the
//         * user has installed Google Play services and returned to the app.
//         */
//
//        locationRequest.apply {
//            interval = TimeUnit.SECONDS.toMillis(7000)
//            fastestInterval = TimeUnit.SECONDS.toMillis(5000)
//            maxWaitTime = TimeUnit.SECONDS.toMillis(30000)
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//
//        /* callback for location result after request */
//        val locationCallback: LocationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                for (location in locationResult.locations) {
//                    if (location != null) {
//
//                        val locationLatitude = location.latitude
//                        val locationLongitude = location.longitude
//
//
//                        val artisanLocation = LatLng(locationLatitude, locationLongitude)
//                        googleMap.addMarker(
//                            MarkerOptions().position(artisanLocation).title("Marker in Sydney")
//                        )
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(artisanLocation))
//
//                        googleMap.setOnMarkerDragListener(object : OnMarkerDragListener {
//                            override fun onMarkerDragStart(marker: Marker) {}
//                            override fun onMarkerDrag(marker: Marker) {}
//                            override fun onMarkerDragEnd(marker: Marker) {
//                                val latLng = marker.position
//                                val geocoder = Geocoder(context, Locale.getDefault())
//                                try {
//                                    val address = geocoder.getFromLocation(
//                                        latLng.latitude,
//                                        latLng.longitude,
//                                        1
//                                    )[0]
//                                } catch (e: IOException) {
//                                    e.printStackTrace()
//                                }
//                            }
//                        })
//                    }
//                }
//            }
//        }
//    }
//
//}

//package com.decagonhq.clads
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.pm.PackageManager
//import android.location.Geocoder
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.app.ActivityCompat
//import com.decagonhq.clads.ui.BaseFragment
//import com.decagonhq.clads.ui.profile.DashboardActivity
//import com.decagonhq.clads.util.Constants.LOCATION_REQUEST_CODE
//import com.decagonhq.clads.util.LocationPermisson
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import dagger.hilt.android.AndroidEntryPoint
//import java.io.IOException
//import java.util.*
//import java.util.concurrent.TimeUnit
//
//
//
//
//@AndroidEntryPoint
//class MapsFragment : BaseFragment() {
//
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private lateinit var locationRequest: LocationRequest
//    private var latitude: Double = 0.0
//    private var longitude: Double = 0.0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        locationRequest = LocationRequest()
//        // initialize fused Location Client to hep in getting location
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//        getLocationPermission()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_maps, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync(callback)
//    }
//
//    private fun getLocationPermission() {
//        if (LocationPermisson.checkPermission(requireActivity() as DashboardActivity)) {
//            // if permission is granted, get the latest updates
//            // getLocationCoOrdinates()
//            showToast("Permission granted")
//        } else {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_REQUEST_CODE
//            )
//        }
//    }
//
////    @SuppressLint("MissingPermission")
////    private fun getLocationCoOrdinates() {
////
//////        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
////
////           }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        // if permission is granted after being denied at first, get update
//        if (grantResults.contains(PackageManager.PERMISSION_GRANTED) && requestCode == LOCATION_REQUEST_CODE) {
//            showToast("Permission granted")
//            //getLocationCoOrdinates()
//        } else {
//            showToast("Location permission is required for this feature to run")
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private val callback = OnMapReadyCallback { googleMap ->
//        /**
//         * Manipulates the map once available.
//         * This callback is triggered when the map is ready to be used.
//         * This is where we can add markers or lines, add listeners or move the camera.
//         * In this case, we just add a marker near Sydney, Australia.
//         * If Google Play services is not installed on the device, the user will be prompted to
//         * install it inside the SupportMapFragment. This method will only be triggered once the
//         * user has installed Google Play services and returned to the app.
//         */
//
//        locationRequest.apply {
//            interval = TimeUnit.SECONDS.toMillis(7000)
//            fastestInterval = TimeUnit.SECONDS.toMillis(5000)
//            maxWaitTime = TimeUnit.SECONDS.toMillis(30000)
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//
//        /* callback for location result after request */
//        val locationCallback: LocationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                for (location in locationResult.locations) {
//                    if (location != null) {
//
//                        Log.d("AAAAA", "$location")
//                        latitude = location.latitude
//                        longitude = location.longitude
//                    }
//                }
//            }
//        }
//
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
//
//
//
//        val artisanLocation = LatLng(latitude, longitude)
//
//        googleMap.addMarker(
//            MarkerOptions()
//                .position(artisanLocation)
//                .title("Marker in ${artisanLocation}")
//                .draggable(true)
//        )
//
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(artisanLocation))
//
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(artisanLocation, 10f))
//
//        googleMap.setOnMarkerDragListener(object : OnMarkerDragListener {
//
//            override fun onMarkerDragStart(marker: Marker) {}
//            override fun onMarkerDrag(marker: Marker) {}
//            override fun onMarkerDragEnd(marker: Marker) {
//                val latLng = marker.position
//                val geocoder = Geocoder(context, Locale.getDefault())
//                try {
//                    val address = geocoder.getFromLocation(
//                        latLng.latitude,
//                        latLng.longitude,
//                        1
//                    )[0]
//
//                    //marker.title = address.subAdminArea
//                    marker.showInfoWindow()
//
//                    Log.d("AAAAA", "${address.locality}")
//
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        })
//    }
//}
