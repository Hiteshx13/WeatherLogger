package com.hitesh.weatherlogger.service.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*


class LocationUtils(var activity: Activity?, private var locationListener: LocationListener) :
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>,
    LocationListener {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 18
        const val TAG = "Location_Provider"
        /**
         * Constant used in the location settings dialog.
         */
        const val REQUEST_LOCATION_SETTINGS = 0x1
        const val UPDATE_INTERVAL_IN_MILLISECONDS = 10000
        const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2
    }

    private var mGoogleApiClient: GoogleApiClient? = null
    /** Parameters for requests to FusedLocationProviderApi.*/
    private var mLocationRequest: LocationRequest? = null
    /**Stores the types of location services*/
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    /** Represents a geographical location.**/
    private var mCurrentLocation: Location? = null

    private var mRequestingLocationUpdates: Boolean = false

    fun isLocationPermissionAllowed(): Boolean {
        return (ContextCompat.checkSelfPermission(
            activity as Context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            activity as Context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    fun init() {
        if (!isLocationPermissionAllowed()
        ) {
            /** Permission is not granted.*/
            if (activity != null) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            /**Location permissions granted*/
            initAllServices()
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                initAllServices()
            }
        }
    }

    private fun initAllServices() {
        /**LocationSettingsRequest objects.**/
        buildGoogleApiClient()
        createLocationRequest()
        buildLocationSettingsRequest()
        mGoogleApiClient?.connect()
        startGettingLocations()
    }

    /** Builds a GoogleApiClient.**/
    private fun buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient")

        mGoogleApiClient = GoogleApiClient.Builder(activity!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(connectionHint: Bundle?) {
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i(TAG, "Connection suspended")
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.errorCode)
    }


    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = UPDATE_INTERVAL_IN_MILLISECONDS.toLong()
        mLocationRequest?.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS.toLong()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        mLocationRequest?.let {
            builder.addLocationRequest(it)
        }
        mLocationSettingsRequest = builder.build()
    }

    private fun checkLocationSettings() {
        val result = LocationServices.SettingsApi.checkLocationSettings(
            mGoogleApiClient, mLocationSettingsRequest
        )
        result.setResultCallback(this)
    }

    override fun onResult(locationSettingsResult: LocationSettingsResult) {
        val status = locationSettingsResult.status
        when (status.statusCode) {
            LocationSettingsStatusCodes.SUCCESS -> {
                Log.i(TAG, "All location settings are satisfied.")
                startLocationUpdates()
            }
            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {

                try {
                    /**Show the dialog by calling startResolutionForResult(), and check the result
                     * in onActivityResult()**/
                    if (activity != null) {
                        status.startResolutionForResult(activity,
                            REQUEST_LOCATION_SETTINGS
                        )
                    }
                } catch (e: IntentSender.SendIntentException) {
                    Log.i(TAG, "PendingIntent unable to execute request.")
                }
            }
            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                Log.d("Location Settings", "SETTINGS_CHANGE_UNAVAILABLE")
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int) {
        when (requestCode) {
            REQUEST_LOCATION_SETTINGS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Log.i(TAG, "User agreed to make required location settings changes.")
                        startLocationUpdates()
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.i(TAG, "User chose not to make required location settings changes.")
                    }
                }
            }
        }
    }

    /**Requests location updates from the FusedLocationApi.**/
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest,
            this
        ).setResultCallback {
            mRequestingLocationUpdates = true
        }
    }

    /**Callback that fires when the location changes.**/
    override fun onLocationChanged(location: Location?) {
        mCurrentLocation = location
        //printLocation()
        locationListener.onLocationReceived(mCurrentLocation)
    }

    private fun startGettingLocations() {
        checkLocationSettings()
    }

    interface LocationListener {
        fun onLocationReceived(location: Location?)
    }
}