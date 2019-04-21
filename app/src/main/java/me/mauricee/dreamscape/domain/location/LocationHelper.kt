package me.mauricee.dreamscape.domain.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import io.reactivex.Maybe
import javax.inject.Inject


class LocationHelper @Inject constructor(private val locationManager: LocationManager, private val context: Context) {

    fun getLocation(): Maybe<Location> = Maybe.defer {
        if (ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.getProviders(true)
                    .map(locationManager::getLastKnownLocation)
                    .filter { it != null }
                    .maxBy { it.accuracy }?.let { Maybe.just(Location(it.longitude, it.latitude)) }
                    ?: Maybe.empty()
        } else {
            Maybe.empty()
        }
    }
}

data class Location(val long: Double, val lat: Double)