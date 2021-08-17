package com.decagonhq.clads.ui.profile.editprofile

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.decagonhq.clads.R
import com.decagonhq.clads.data.domain.images.UserProfileImage
import com.decagonhq.clads.data.domain.profile.Union
import com.decagonhq.clads.data.domain.profile.UserProfile
import com.decagonhq.clads.data.domain.profile.WorkshopAddress
import com.decagonhq.clads.data.local.UserProfileEntity
import com.decagonhq.clads.databinding.AccountFragmentBinding
import com.decagonhq.clads.ui.BaseFragment
import com.decagonhq.clads.ui.profile.dialogfragment.ProfileManagementDialogFragments.Companion.createProfileDialogFragment
import com.decagonhq.clads.util.Resource
import com.decagonhq.clads.util.checkGPSEnabled
import com.decagonhq.clads.util.handleApiError
import com.decagonhq.clads.util.loadImage
import com.decagonhq.clads.util.observeOnce
import com.decagonhq.clads.util.saveBitmap
import com.decagonhq.clads.util.uriToBitmap
import com.decagonhq.clads.viewmodels.ImageUploadViewModel
import com.decagonhq.clads.viewmodels.UserProfileViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AccountFragment : BaseFragment() {

    private var _binding: AccountFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val imageUploadViewModel: ImageUploadViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationManager: LocationManager
    var gps_enabled = false
    var network_enabled = false

    private lateinit var geocoder: Geocoder
    private lateinit var locationRequest: LocationRequest
    private lateinit var addresses: MutableList<Address>
    private var artisanLatitude: Double = 0.0
    private var artisanLongitude: Double = 0.0
    private var artisanStreet: String = ""
    private var artisanCity: String = ""
    private var artisanState: String = ""
    private val LOCATION_REQUEST_CODE = 1

    /* set broadcast receiver object */
        var broadcastReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                /* listen for changes in cell broadcast */
                if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    if (isGpsEnabled) {
                        // Handle Location turned ON
                        getArtisanLocation()
                        Toast.makeText(requireContext(), "LOCATION ENABLED", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), "LOCATION DISABLED", Toast.LENGTH_LONG).show()
                        // Handle Location turned OFF
                    }
                }
            }
        }

    override fun onStart() {
        super.onStart()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* register broadcast receivers */
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        requireContext().registerReceiver(broadcastReceiver, filter)
        showToast("Registered")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = AccountFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* initialize location request */
        locationRequest = LocationRequest()
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        /*Dialog fragment functions*/
        accountFirstNameEditDialog()
        accountGenderSelectDialog()
        accountUnionStateDialogFragment()
        accountUnionLGADialogFragment()
        accountUnionWardDialogFragment()
        accountUnionNameDialogFragment()
        accountLastNameDialogFragment()
        accountEmployeeNumberDialogFragment()
        accountOtherNameEditDialog()
        accountLegalStatusDialog()

        /*Initialize Image Cropper*/
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                binding.accountFragmentEditProfileIconImageView.imageAlpha = 140
                uploadImageToServer(uri)
            }
        }

        // inflate bottom sheet
        binding.accountFragmentWorkshopAddressValueTextView.setOnClickListener {

            if (binding.accountFragmentWorkshopAddressValueTextView.text.isNullOrEmpty()) {

                val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)

                val bottomSheetView: View = layoutInflater.inflate(
                    R.layout.account_fragment_add_address_bottom_sheet,
                    view.findViewById(R.id.account_fragment_location_bottom_sheet) as LinearLayout?
                )

                val setLocationLaterRadioButton: RadioButton? =
                    bottomSheetView.findViewById(R.id.account_fragment_location_bottom_sheet_set_location_later_radio_button)

                val setLocationNowRadioButton: RadioButton? =
                    bottomSheetView.findViewById(R.id.account_fragment_location_bottom_sheet_set_location_now_radio_button)

                setLocationNowRadioButton?.setOnClickListener {
                    dialog.dismiss()
                    if (binding.accountFragmentWorkshopAddressValueTextView.text.isNullOrEmpty()) {
                        initializeLocations()
                    }
                }

                setLocationLaterRadioButton?.setOnClickListener {
                    showToast(setLocationLaterRadioButton.text as String)
                    dialog.dismiss()
                }

                dialog.setContentView(bottomSheetView)
                dialog.show()
            } else {
                // EDIT ADDRESS HERE
                showToast("EDIT ADDRESS HERE")
            }
        }

        /*Select profile image*/
        binding.accountFragmentEditProfileIconImageView.setOnClickListener {
            Manifest.permission.READ_EXTERNAL_STORAGE.checkForPermission(NAME, READ_IMAGE_STORAGE)
        }

        /* Update User Profile */
        binding.accountFragmentSaveChangesButton.setOnClickListener {
            updateUserProfile()
        }

        /*Get users profile*/
        userProfileViewModel.getLocalDatabaseUserProfile()
        getUserProfile()
    }

    /*Get User Profile*/
    private fun getUserProfile() {
        userProfileViewModel.userProfile.observe(
            viewLifecycleOwner,
            Observer {
                if (it is Resource.Loading && it.data?.firstName.isNullOrEmpty()) {
                    progressDialog.showDialogFragment("Updating..")
                } else if (it is Resource.Error) {
                    progressDialog.hideProgressDialog()
                    handleApiError(it, mainRetrofit, requireView(), sessionManager, database)
                } else {
                    progressDialog.hideProgressDialog()
                    it.data?.let { userProfile ->
                        binding.apply {
                            accountFragmentFirstNameValueTextView.text = userProfile.firstName
                            accountFragmentLastNameValueTextView.text = userProfile.lastName
                            accountFragmentPhoneNumberValueTextView.text = userProfile.phoneNumber
                            accountFragmentGenderValueTextView.text = userProfile.gender
                            if (userProfile.workshopAddress?.street == null) {
                                accountFragmentWorkshopAddressValueTextView.hint =
                                    // ---------------------------------------------------//
                                    "Tap to enter shop address"
                            } else {
                                accountFragmentWorkshopAddressValueTextView.text =
                                    "${userProfile.workshopAddress?.street}, ${userProfile.workshopAddress?.city}, ${userProfile.workshopAddress?.state}."
                            }
                            accountFragmentNameOfUnionValueTextView.text = userProfile.union?.name
                            accountFragmentWardValueTextView.text = userProfile.union?.ward
                            accountFragmentLocalGovtAreaValueTextView.text = userProfile.union?.lga
                            accountFragmentStateValueTextView.text = userProfile.union?.state
                            /*Load Profile Picture with Glide*/
                            binding.accountFragmentEditProfileIconImageView.loadImage(userProfile.thumbnail)
                        }
                    }
                }
            }
        )
    }

    /*Update User Profile*/
    private fun updateUserProfile() {

        userProfileViewModel.userProfile.observeOnce(
            viewLifecycleOwner,
            {
                if (it is Resource.Loading && it.data?.firstName.isNullOrEmpty()) {
                    progressDialog.showDialogFragment("Fetching profile data...")
                } else if (it is Resource.Error) {
                    progressDialog.hideProgressDialog()
                    handleApiError(it, mainRetrofit, requireView(), sessionManager, database)
                } else {

                    progressDialog.hideProgressDialog()
                    showToast("Update successful")

                    it.data?.let { profile ->

                        val userProfile = UserProfile(
                            country = profile.country,
                            deliveryTime = profile.deliveryTime,
                            email = profile.email,
                            firstName = binding.accountFragmentFirstNameValueTextView.text.toString(),
                            gender = binding.accountFragmentGenderValueTextView.text.toString(),
                            genderFocus = profile.genderFocus,
                            lastName = binding.accountFragmentLastNameValueTextView.text.toString(),
                            measurementOption = profile.measurementOption,
                            phoneNumber = binding.accountFragmentPhoneNumberValueTextView.text.toString(),
                            role = profile.role,
                            workshopAddress = WorkshopAddress(
                                artisanStreet,
                                artisanCity,
                                artisanState,
                                artisanLongitude.toString(),
                                artisanLatitude.toString()
                            ),

                            specialties = profile.specialties,
                            thumbnail = profile.thumbnail,
                            trained = profile.trained,
                            union = Union(
                                name = binding.accountFragmentNameOfUnionValueTextView.text.toString(),
                                ward = binding.accountFragmentWardValueTextView.text.toString(),
                                lga = binding.accountFragmentLocalGovtAreaValueTextView.text.toString(),
                                state = binding.accountFragmentStateValueTextView.text.toString(),
                            ),
                            paymentTerms = profile.paymentTerms,
                            paymentOptions = profile.paymentOptions
                        )

                        userProfileViewModel.updateUserProfile(userProfile)

                        /* remove location update calls */
                        fusedLocationProvider.removeLocationUpdates(locationCallback)
                    }
                }
            }
        )
    }

    /*Update User Profile Picture*/
    private fun updateUserProfilePicture(downloadUri: String) {
        userProfileViewModel.userProfile.observeOnce(
            viewLifecycleOwner,
            Observer {
                if (it is Resource.Loading<UserProfileEntity>) {
                    progressDialog.showDialogFragment("Uploading...")
                } else if (it is Resource.Error) {
                    progressDialog.hideProgressDialog()
                    handleApiError(it, mainRetrofit, requireView(), sessionManager, database)
                } else {
//                    progressDialog.hideProgressDialog()
                    it.data?.let { profile ->
                        val userProfile = UserProfile(
                            country = profile.country,
                            deliveryTime = profile.deliveryTime,
                            email = profile.email,
                            firstName = profile.firstName,
                            gender = profile.gender,
                            genderFocus = profile.genderFocus,
                            lastName = profile.lastName,
                            measurementOption = profile.measurementOption,
                            phoneNumber = profile.phoneNumber,
                            role = profile.role,
                            workshopAddress = profile.workshopAddress,
                            showroomAddress = profile.showroomAddress,
                            specialties = profile.specialties,
                            thumbnail = downloadUri,
                            trained = profile.trained,
                            union = profile.union,
                            paymentTerms = profile.paymentTerms,
                            paymentOptions = profile.paymentOptions
                        )
                        userProfileViewModel.updateUserProfile(userProfile)
                    }
                }
            }
        )
    }

    /*Check for Gallery Permission*/
    private fun String.checkForPermission(name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    this
                ) == PackageManager.PERMISSION_GRANTED -> {

                    cropActivityResultLauncher.launch(null)
                }
                shouldShowRequestPermissionRationale(this) -> showDialog(this, name, requestCode)
                else -> ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(this),
                    requestCode
                )
            }
        }
    }

    // check for permission and make call
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {

            READ_IMAGE_STORAGE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("permission refused")
                } else {
                    showToast("Permission granted")
                    cropActivityResultLauncher.launch(null)
                }
            }

            LOCATION_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("permission refused")
                } else {
                    checkGPSEnabled(LOCATION_REQUEST_CODE) { broadcastReceiver }
                }
            }
        }
    }

    // Show dialog for permission dialog
    private fun showDialog(permission: String, name: String, requestCode: Int) {
        // Alert dialog box
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            // setting alert properties
            setMessage(getString(R.string.permision_to_access) + name + getString(R.string.is_required_to_use_this_app))
            setTitle("Permission required")
            setPositiveButton("Ok") { _, _ ->
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    /*function to crop picture*/
    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setCropMenuCropButtonTitle("Done")
                .setAspectRatio(1, 1)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            var imageUri: Uri? = null
            try {
                imageUri = CropImage.getActivityResult(intent).uri
            } catch (e: Exception) {
                Timber.d(e.localizedMessage)
            }
            return imageUri
        }
    }

    /*Upload Profile Picture*/
    private fun uploadImageToServer(uri: Uri) {

        // create RequestBody instance from file
        val convertedImageUriToBitmap = uriToBitmap(uri)
        val bitmapToFile = saveBitmap(convertedImageUriToBitmap)

        /*Compress Image then Upload Image*/
        lifecycleScope.launch {
            val compressedImage = Compressor.compress(requireContext(), bitmapToFile!!)
            val imageBody = compressedImage.asRequestBody("image/jpg".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("file", bitmapToFile?.name, imageBody!!)
            imageUploadViewModel.mediaImageUpload(image)
        }

        /*Handling the response from the retrofit*/
        imageUploadViewModel.userProfileImage.observe(
            viewLifecycleOwner,
            Observer {
                if (it is Resource.Loading<UserProfileImage>) {
                    progressDialog.showDialogFragment("Uploading...")
                } else if (it is Resource.Error) {
                    progressDialog.hideProgressDialog()
                    handleApiError(it, mainRetrofit, requireView(), sessionManager, database)
                } else {
//                    progressDialog.hideProgressDialog()
                    showToast("Upload Successful")
                    it.data?.downloadUri?.let { imageUrl ->
                        updateUserProfilePicture(imageUrl)
                        /*Load Profile Picture with Glide*/
                        binding.accountFragmentEditProfileIconImageView.loadImage(imageUrl)
                    }
                }
            }
        )
    }

    private fun accountLegalStatusDialog() {
        childFragmentManager.setFragmentResultListener(
            ACCOUNT_LEGAL_STATUS_REQUEST_KEY,
            requireActivity()
        ) { key, bundle ->
            // collect input values from dialog fragment and update the firstname text of user
            val legalStatus = bundle.getString(ACCOUNT_LEGAL_STATUS_BUNDLE_KEY)
            binding.accountFragmentLegalStatusValueTextView.text = legalStatus
        }

        // when employee number name value is clicked
        binding.accountFragmentLegalStatusValueTextView.setOnClickListener {
            val currentLegalStatus =
                binding.accountFragmentLegalStatusValueTextView.text.toString()
            val bundle = bundleOf(CURRENT_ACCOUNT_LEGAL_STATUS_BUNDLE_KEY to currentLegalStatus)
            createProfileDialogFragment(
                R.layout.account_legal_status_dialog_fragment,
                bundle
            ).show(
                childFragmentManager, AccountFragment::class.java.simpleName
            )
        }
    }

    // firstName Dialog
    private fun accountFirstNameEditDialog() {
        // when first name value is clicked
        childFragmentManager.setFragmentResultListener(
            ACCOUNT_FIRST_NAME_REQUEST_KEY,
            requireActivity()
        ) { key, bundle ->
            // collect input values from dialog fragment and update the firstname text of user
            val firstName = bundle.getString(ACCOUNT_FIRST_NAME_BUNDLE_KEY)
            binding.accountFragmentFirstNameValueTextView.text = firstName
        }

        // when first name value is clicked
        binding.accountFragmentFirstNameValueTextView.setOnClickListener {
            val currentFirstName = binding.accountFragmentFirstNameValueTextView.text.toString()
            val bundle = bundleOf(CURRENT_ACCOUNT_FIRST_NAME_BUNDLE_KEY to currentFirstName)
            createProfileDialogFragment(
                R.layout.account_first_name_dialog_fragment,
                bundle
            ).show(
                childFragmentManager, getString(R.string.frstname_dialog_fragment)
            )
        }
    }

    private fun accountLastNameDialogFragment() {
        // when last name value is clicked
        childFragmentManager.setFragmentResultListener(
            ACCOUNT_LAST_NAME_REQUEST_KEY,
            requireActivity()
        ) { key, bundle ->
            // collect input values from dialog fragment and update the firstname text of user
            val lastName = bundle.getString(ACCOUNT_LAST_NAME_BUNDLE_KEY)
            binding.accountFragmentLastNameValueTextView.text = lastName
        }

        // when last Name name value is clicked
        binding.accountFragmentLastNameValueTextView.setOnClickListener {
            val currentLastName = binding.accountFragmentLastNameValueTextView.text.toString()
            val bundle = bundleOf(CURRENT_ACCOUNT_LAST_NAME_BUNDLE_KEY to currentLastName)
            createProfileDialogFragment(
                R.layout.account_last_name_dialog_fragment,
                bundle
            ).show(
                childFragmentManager, AccountFragment::class.java.simpleName
            )
        }
    }

    // Other name Dialog
    private fun accountOtherNameEditDialog() {
        // when other name name value is clicked
        childFragmentManager.setFragmentResultListener(
            ACCOUNT_OTHER_NAME_REQUEST_KEY,
            requireActivity()
        ) { key, bundle ->
            // collect input values from dialog fragment and update the otherName text of user
            val otherName = bundle.getString(ACCOUNT_OTHER_NAME_BUNDLE_KEY)
            binding.accountFragmentPhoneNumberValueTextView.text = otherName
        }

        // when last Name name value is clicked
        binding.accountFragmentPhoneNumberValueTextView.setOnClickListener {
            val currentOtherName =
                binding.accountFragmentPhoneNumberValueTextView.text.toString()
            val bundle = bundleOf(CURRENT_ACCOUNT_OTHER_NAME_BUNDLE_KEY to currentOtherName)
            createProfileDialogFragment(
                R.layout.account_phone_number_dialog_fragment,
                bundle
            ).show(
                childFragmentManager, AccountFragment::class.java.simpleName
            )
        }
    }

    private fun accountEmployeeNumberDialogFragment() {
        childFragmentManager.setFragmentResultListener(
            ACCOUNT_EMPLOYEE_REQUEST_KEY,
            requireActivity()
        ) { key, bundle ->
            // collect input values from dialog fragment and update the employee number text of user
            val employeeNumber = bundle.getString(ACCOUNT_EMPLOYEE_BUNDLE_KEY)
            binding.accountFragmentNumberOfEmployeeApprenticeValueTextView.text = employeeNumber
        }

        // when employee number name value is clicked
        binding.accountFragmentNumberOfEmployeeApprenticeValueTextView.setOnClickListener {
            val currentEmployeeNumber =
                binding.accountFragmentNumberOfEmployeeApprenticeValueTextView.text.toString()
            val bundle = bundleOf(CURRENT_ACCOUNT_EMPLOYEE_BUNDLE_KEY to currentEmployeeNumber)
            createProfileDialogFragment(
                R.layout.account_employee_number_dialog_fragment,
                bundle
            ).show(
                childFragmentManager,
                getString(R.string.tag_employee_number_dialog_fragment)
            )
        }
    }

    private fun accountUnionNameDialogFragment() {
        // when union name value is clicked
        childFragmentManager.setFragmentResultListener(
            ACCOUNT_UNION_NAME_REQUEST_KEY,
            requireActivity()
        ) { key, bundle ->
            // collect input values from dialog fragment and update the union name text of user
            val unionName = bundle.getString(ACCOUNT_UNION_NAME_BUNDLE_KEY)
            binding.accountFragmentNameOfUnionValueTextView.text = unionName
        }

        // when union name value is clicked
        binding.accountFragmentNameOfUnionValueTextView.setOnClickListener {
            val currentUnionName =
                binding.accountFragmentNameOfUnionValueTextView.text.toString()
            val bundle = bundleOf(CURRENT_ACCOUNT_UNION_NAME_BUNDLE_KEY to currentUnionName)
            createProfileDialogFragment(
                R.layout.account_union_name_dialog_fragment,
                bundle
            ).show(
                childFragmentManager,
                AccountFragment::class.java.simpleName
            )
        }
    }

    private fun accountUnionWardDialogFragment() {
        // when ward name value is clicked
        childFragmentManager.setFragmentResultListener(
            ACCOUNT_UNION_WARD_REQUEST_KEY,
            requireActivity()
        ) { key, bundle ->
            // collect input values from dialog fragment and update the union name text of user
            val unionWard = bundle.getString(ACCOUNT_UNION_WARD_BUNDLE_KEY)
            binding.accountFragmentWardValueTextView.text = unionWard
        }

        // when union ward value is clicked
        binding.accountFragmentWardValueTextView.setOnClickListener {
            val currentUnionWard =
                binding.accountFragmentWardValueTextView.text.toString()
            val bundle = bundleOf(CURRENT_ACCOUNT_UNION_WARD_BUNDLE_KEY to currentUnionWard)
            createProfileDialogFragment(
                R.layout.account_union_ward_dialog_fragment,
                bundle
            ).show(
                childFragmentManager,
                AccountFragment::class.java.simpleName
            )
        }
    }

    private fun accountUnionLGADialogFragment() {
        // when lga name value is clicked
        childFragmentManager.setFragmentResultListener(
            ACCOUNT_UNION_LGA_REQUEST_KEY,
            requireActivity()
        ) { key, bundle ->
            // collect input values from dialog fragment and update the union lga text of user
            val unionLga = bundle.getString(ACCOUNT_UNION_LGA_BUNDLE_KEY)
            binding.accountFragmentLocalGovtAreaValueTextView.text = unionLga
        }

        // when lga value is clicked
        binding.accountFragmentLocalGovtAreaValueTextView.setOnClickListener {
            val currentUnionState =
                binding.accountFragmentLocalGovtAreaValueTextView.text.toString()
            val bundle = bundleOf(CURRENT_ACCOUNT_UNION_LGA_BUNDLE_KEY to currentUnionState)
            createProfileDialogFragment(
                R.layout.account_union_lga_dialog_fragment,
                bundle
            ).show(
                childFragmentManager,
                AccountFragment::class.java.simpleName
            )
        }
    }

    private fun accountUnionStateDialogFragment() {
        // when state name value is clicked
        childFragmentManager.setFragmentResultListener(
            ACCOUNT_UNION_STATE_REQUEST_KEY,
            requireActivity()
        ) { key, bundle ->
            // collect input values from dialog fragment and update the union state text of user
            val unionState = bundle.getString(ACCOUNT_UNION_STATE_BUNDLE_KEY)
            binding.accountFragmentStateValueTextView.text = unionState
        }

        // when state value is clicked
        binding.accountFragmentStateValueTextView.setOnClickListener {
            val currentUnionState =
                binding.accountFragmentStateValueTextView.text.toString()
            val bundle = bundleOf(CURRENT_ACCOUNT_STATE_NAME_BUNDLE_KEY to currentUnionState)
            createProfileDialogFragment(
                R.layout.account_union_state_dialog_fragment,
                bundle
            ).show(
                childFragmentManager,
                AccountFragment::class.java.simpleName
            )
        }
    }

    private fun initializeLocations() { // ---------------------------------------------------//

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        } else {
            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

                if (gps_enabled) {
                    getArtisanLocation()
                } else {
                    checkGPSEnabled(LOCATION_REQUEST_CODE) { broadcastReceiver }
                }
            } catch (ex: java.lang.Exception) {
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getArtisanLocation() {

        progressDialog.showDialogFragment("fetching your location")
        /* set location request necessities */
        locationRequest.apply {
            interval = TimeUnit.SECONDS.toMillis(60000)
            fastestInterval = TimeUnit.SECONDS.toMillis(5000)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            /* callback for location result after request */
            val locationCallback: LocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    for (location in locationResult.locations) {
                        if (location != null) {

                            val locationLatitude = location.latitude
                            val locationLongitude = location.longitude

                            /* use co-ordinates to get address */
                            addresses = geocoder.getFromLocation(
                                locationLatitude,
                                locationLongitude,
                                1
                            )

                            /* extract address */
                            artisanStreet =
                                "${addresses[0].featureName} ${addresses[0].thoroughfare}, ${addresses[0].subAdminArea}"
                            var locality = addresses[0].subAdminArea
                            artisanCity = addresses[0].locality
                            artisanState = addresses[0].adminArea
                            artisanLatitude = addresses[0].latitude
                            artisanLongitude = addresses[0].longitude

                            binding.accountFragmentWorkshopAddressValueTextView.text =
                                "${addresses[0].featureName}, ${addresses[0].thoroughfare}, $locality, $artisanCity, $artisanState"

                            if(binding.accountFragmentWorkshopAddressValueTextView.text.isNotEmpty()){
                                progressDialog.hideProgressDialog()
                            }

                        }
                    }
                }
            }

            /* initialize geoCoder */
            geocoder = Geocoder(requireContext(), Locale.getDefault())

            /* initialize fusedLocation client */
            fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireContext())

            /* actual location request */
            fusedLocationProvider.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    // Gender Dialog
    private fun accountGenderSelectDialog() {
        // when gender value is clicked
        childFragmentManager.setFragmentResultListener(
            ACCOUNT_GENDER_REQUEST_KEY,
            requireActivity()
        ) { key, bundle ->
            // collect input values from dialog fragment and update the text of user
            val gender = bundle.getString(ACCOUNT_GENDER_BUNDLE_KEY)
            binding.accountFragmentGenderValueTextView.text = gender
        }

        // when employee number name value is clicked
        binding.accountFragmentGenderValueTextView.setOnClickListener {
            val currentGender = binding.accountFragmentGenderValueTextView.text.toString()
            val bundle = bundleOf(CURRENT_ACCOUNT_GENDER_BUNDLE_KEY to currentGender)
            createProfileDialogFragment(R.layout.account_gender_dialog_fragment, bundle).show(
                childFragmentManager, AccountFragment::class.java.simpleName
            )
        }
    }

    override fun onPause() {
        super.onPause()
        /* unregister from broadcast receiver */
        requireContext().unregisterReceiver(broadcastReceiver)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ACCOUNT_EMPLOYEE_REQUEST_KEY = "ACCOUNT EMPLOYEE REQUEST KEY"
        const val ACCOUNT_EMPLOYEE_BUNDLE_KEY = "ACCOUNT EMPLOYEE BUNDLE KEY"
        const val CURRENT_ACCOUNT_EMPLOYEE_BUNDLE_KEY = "CURRENT ACCOUNT EMPLOYEE BUNDLE KEY"

        const val ACCOUNT_FIRST_NAME_REQUEST_KEY = "ACCOUNT FIRST NAME REQUEST KEY"
        const val ACCOUNT_FIRST_NAME_BUNDLE_KEY = "ACCOUNT FIRST NAME BUNDLE KEY"
        const val CURRENT_ACCOUNT_FIRST_NAME_BUNDLE_KEY = "CURRENT ACCOUNT FIRST NAME BUNDLE KEY"

        const val ACCOUNT_LAST_NAME_REQUEST_KEY = "ACCOUNT LAST NAME REQUEST KEY"
        const val ACCOUNT_LAST_NAME_BUNDLE_KEY = "ACCOUNT LAST NAME BUNDLE KEY"
        const val CURRENT_ACCOUNT_LAST_NAME_BUNDLE_KEY = "CURRENT ACCOUNT LAST NAME BUNDLE KEY"

        const val ACCOUNT_OTHER_NAME_REQUEST_KEY = "ACCOUNT OTHER NAME REQUEST KEY"
        const val ACCOUNT_OTHER_NAME_BUNDLE_KEY = "ACCOUNT OTHER NAME BUNDLE KEY"
        const val CURRENT_ACCOUNT_OTHER_NAME_BUNDLE_KEY = "CURRENT ACCOUNT OTHER NAME BUNDLE KEY"

        const val ACCOUNT_GENDER_REQUEST_KEY = "ACCOUNT GENDER REQUEST KEY"
        const val ACCOUNT_GENDER_BUNDLE_KEY = "ACCOUNT GENDER BUNDLE KEY"
        const val CURRENT_ACCOUNT_GENDER_BUNDLE_KEY = "CURRENT ACCOUNT GENDER BUNDLE KEY"

        const val ACCOUNT_WORKSHOP_STATE_REQUEST_KEY = "ACCOUNT WORKSHOP STATE REQUEST KEY"
        const val ACCOUNT_WORKSHOP_STATE_BUNDLE_KEY = "ACCOUNT WORKSHOP STATE BUNDLE KEY"
        const val CURRENT_ACCOUNT_WORKSHOP_STATE_BUNDLE_KEY =
            "CURRENT ACCOUNT WORKSHOP STATE BUNDLE KEY"

        const val ACCOUNT_WORKSHOP_CITY_REQUEST_KEY = "ACCOUNT WORKSHOP CITY REQUEST KEY"
        const val ACCOUNT_WORKSHOP_CITY_BUNDLE_KEY = "ACCOUNT WORKSHOP CITY BUNDLE KEY"
        const val CURRENT_ACCOUNT_WORKSHOP_CITY_BUNDLE_KEY =
            "CURRENT ACCOUNT WORKSHOP CITY BUNDLE KEY"

        const val ACCOUNT_WORKSHOP_STREET_REQUEST_KEY = "ACCOUNT WORKSHOP STREET REQUEST KEY"
        const val ACCOUNT_WORKSHOP_STREET_BUNDLE_KEY = "ACCOUNT WORKSHOP STREET BUNDLE KEY"
        const val CURRENT_ACCOUNT_WORKSHOP_STREET_BUNDLE_KEY =
            "CURRENT ACCOUNT WORKSHOP STREET BUNDLE KEY"

        const val ACCOUNT_SHOWROOM_ADDRESS_REQUEST_KEY = "ACCOUNT SHOWROOM ADDRESS REQUEST KEY"
        const val ACCOUNT_SHOWROOM_ADDRESS_BUNDLE_KEY = "ACCOUNT SHOWROOM ADDRESS BUNDLE KEY"
        const val CURRENT_ACCOUNT_SHOWROOM_ADDRESS_BUNDLE_KEY =
            "CURRENT ACCOUNT SHOWROOM ADDRESS BUNDLE KEY"

        const val ACCOUNT_UNION_NAME_REQUEST_KEY = "ACCOUNT UNION NAME REQUEST KEY"
        const val ACCOUNT_UNION_NAME_BUNDLE_KEY = "ACCOUNT UNION NAME BUNDLE KEY"
        const val CURRENT_ACCOUNT_UNION_NAME_BUNDLE_KEY = "CURRENT ACCOUNT UNION NAME BUNDLE KEY"

        const val ACCOUNT_UNION_STATE_REQUEST_KEY = "ACCOUNT UNION STATE REQUEST KEY"
        const val ACCOUNT_UNION_STATE_BUNDLE_KEY = "ACCOUNT UNION STATE BUNDLE KEY"
        const val CURRENT_ACCOUNT_STATE_NAME_BUNDLE_KEY = "CURRENT ACCOUNT UNION STATE BUNDLE KEY"

        const val ACCOUNT_UNION_WARD_REQUEST_KEY = "ACCOUNT UNION WARD REQUEST KEY"
        const val ACCOUNT_UNION_WARD_BUNDLE_KEY = "ACCOUNT UNION WARD BUNDLE KEY"
        const val CURRENT_ACCOUNT_UNION_WARD_BUNDLE_KEY = "CURRENT ACCOUNT UNION WARD BUNDLE KEY"

        const val ACCOUNT_UNION_LGA_REQUEST_KEY = "ACCOUNT UNION LGA REQUEST KEY"
        const val ACCOUNT_UNION_LGA_BUNDLE_KEY = "ACCOUNT UNION LGA BUNDLE KEY"
        const val CURRENT_ACCOUNT_UNION_LGA_BUNDLE_KEY = "CURRENT ACCOUNT UNION LGA BUNDLE KEY"

        const val ACCOUNT_LEGAL_STATUS_REQUEST_KEY = "ACCOUNT LEGAL STATUS REQUEST KEY"
        const val ACCOUNT_LEGAL_STATUS_BUNDLE_KEY = "ACCOUNT LEGAL STATUS BUNDLE KEY"
        const val CURRENT_ACCOUNT_LEGAL_STATUS_BUNDLE_KEY =
            "CURRENT ACCOUNT LEGAL STATUS BUNDLE KEY"

        const val RENAME_DESCRIPTION_REQUEST_KEY = "ACCOUNT FIRST NAME REQUEST KEY"
        const val RENAME_DESCRIPTION_BUNDLE_KEY = "ACCOUNT FIRST NAME BUNDLE KEY"
        const val CURRENT_ACCOUNT_RENAME_DESCRIPTION_BUNDLE_KEY =
            "CURRENT ACCOUNT FIRST NAME BUNDLE KEY"

        const val READ_IMAGE_STORAGE = 102
        const val NAME = "CLads"

        const val GPS_KEY = "false"
    }
}
