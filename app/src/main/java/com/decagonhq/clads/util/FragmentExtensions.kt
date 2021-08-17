package com.decagonhq.clads.util

import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.decagonhq.clads.R
import com.decagonhq.clads.data.local.CladsDatabase
import com.decagonhq.clads.ui.authentication.MainActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

fun Fragment.showView(view: View) {

    if (view.visibility == View.GONE || view.visibility == View.INVISIBLE) {
        view.visibility = View.VISIBLE
    }
}

fun Fragment.navigateTo(id: Int) {
    findNavController().navigate(id)
}

fun Fragment.navigateTo(direction: NavDirections) {
    findNavController().navigate(direction)
}

fun <T> Fragment.handleApiError(
    failure: Resource.Error<T>,
    retrofit: Retrofit,
    view: View,
    sessionManager: SessionManager,
    database: CladsDatabase
) {
    val errorResponseUtil = ErrorResponseUtil(retrofit)
    when (failure.isNetworkError) {
        true -> {
            view.showSnackBar("Poor Internet Connection. Retry")
        }
        else -> {
            try {
                val error = failure.errorBody?.let { it1 -> errorResponseUtil.parseError(it1) }
                val errorMessage = error?.message
                if (errorMessage != null) {
                    if (errorMessage == "Invalid username/password" && findNavController().currentDestination?.id != R.id.login_fragment) {
                        logOut(sessionManager, database)
                    } else if (errorMessage == "401") {
                        view.showSnackBar("Session Timeout")
                        logOut(sessionManager, database)
                    } else {
                        view.showSnackBar(errorMessage)
                    }
                } else {
                    view.showSnackBar("Something went wrong!... Retry")
                }
            } catch (e: Exception) {
                view.showSnackBar("Bad request. Check Input again.")
            }
        }
    }
}

fun Fragment.logOut(sessionManager: SessionManager, database: CladsDatabase) {
    Intent(requireActivity(), MainActivity::class.java).also {
        sessionManager.clearSharedPref()
        sessionManager.saveToSharedPref(
            getString(R.string.login_status),
            getString(R.string.log_out)
        )
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                database.clearAllTables()
            }
        }
        startActivity(it)
        requireActivity().finish()
    }
}

/*Extension function to observe Live Data only once*/
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(
        lifecycleOwner,
        object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        }
    )
}

/*Function to Show Progress Dialog*/
fun Fragment.showLoadingBar(message: String): Dialog {
    val dialog by lazy {
        Dialog(requireContext(), R.style.Theme_MaterialComponents_Dialog).apply {
            setContentView(R.layout.layout_loading_dialog)
            setCanceledOnTouchOutside(false)
            setTitle(message)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
    return dialog
}

fun Fragment.checkGPSEnabled(LOCATION_REQUEST_CODE: Int, action: () -> Unit) {

    val locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(7000)
            fastestInterval = TimeUnit.SECONDS.toMillis(5000)
            maxWaitTime = TimeUnit.SECONDS.toMillis(30000)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

    val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
    val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

    task.addOnSuccessListener { locationSettingsResponse ->

        action.invoke()
    }

    task.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                /** Make alert dialog to request user to turn on GPS**/
                android.app.AlertDialog.Builder(requireContext())
                    .setTitle("GPS Settings")
                    .setMessage("GPS is off. App requires location turned on for verification.")
                    .setPositiveButton(
                        "SETTINGS"
                    ) { dialogInterface, i ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivityForResult(intent, LOCATION_REQUEST_CODE)
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialogInterface, i ->
                        Toast.makeText(
                            requireContext(),
                            "GPS is off. App requires location turned on for verification.",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialogInterface.cancel()
                    }
                    .create()
                    .show()
            } catch (sendEx: IntentSender.SendIntentException) {
                // Ignore the error.
            }
        }
    }
}
