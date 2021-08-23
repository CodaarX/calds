package com.decagonhq.clads

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.decagonhq.clads.data.domain.profile.ArtisanAddress
import com.decagonhq.clads.databinding.FragmentMapsBinding
import com.decagonhq.clads.ui.BaseFragment
import com.decagonhq.clads.viewmodels.ArtisanLocationViewModel
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
    private val artisanLocationViewModel: ArtisanLocationViewModel by activityViewModels()
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var geocoder: Geocoder
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var extractedLocation: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationRequest = LocationRequest()
        // initialize fused Location Client to hep in getting location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        initializeLocations()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initializeLocations() {
        getLocationCoOrdinates()
        geocoder = Geocoder(context, Locale.getDefault())
    }

    @SuppressLint("MissingPermission")
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

                    latitude = location[0].latitude
                    longitude = location[0].longitude
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        googleMap.isMyLocationEnabled = true

        val artisanLocation = LatLng(latitude, longitude)

        googleMap.setOnMapClickListener {
            try {
                val address = geocoder.getFromLocation(
                    it.latitude,
                    it.longitude,
                    1
                )[0]

                val location =
                    "${address.featureName} ${address.thoroughfare}, ${address.subAdminArea}"
                extractedLocation = address

                googleMap.clear()
                googleMap.addMarker(
                    MarkerOptions()
                        .position(it)
                        .draggable(true)
                        .title("Current location:: $location")
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

                val location =
                    "${address.featureName} ${address.thoroughfare}, ${address.subAdminArea}"
                extractedLocation = address

                googleMap.addMarker(
                    MarkerOptions()
                        .position(it)
                        .draggable(true)
                        .title("Current location: $location")
                ).showInfoWindow()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

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
                    extractedLocation = address


                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })


        if (artisanLocation.latitude != 0.0) {

            try {
                val address = geocoder.getFromLocation(
                    artisanLocation.latitude,
                    artisanLocation.longitude,
                    1
                )[0]

                val location =
                    "${address.featureName} ${address.thoroughfare}, ${address.subAdminArea}"
                extractedLocation = address


                googleMap.addMarker(
                    MarkerOptions()
                        .position(artisanLocation)
                        .title("Current location: $location")
                        .draggable(true)
                ).showInfoWindow()


            } catch (e: IOException) {
                e.printStackTrace()
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(artisanLocation))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(artisanLocation, 15f))
        }


        val mapView = View(requireContext())
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

        binding.mapFragmentSaveLocationButton.setOnClickListener {

            if (extractedLocation.latitude != null &&
                extractedLocation.longitude != null
            ) {

                val artisanAddress = ArtisanAddress(
                    extractedLocation.locality,
                    extractedLocation.featureName,
                    extractedLocation.thoroughfare,
                    extractedLocation.subAdminArea,
                    extractedLocation.latitude,
                    extractedLocation.longitude
                )

                artisanLocationViewModel.setArtisanAddress(artisanAddress)
                findNavController().navigate(R.id.action_mapsFragment_to_editProfileFragment)
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}