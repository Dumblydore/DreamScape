package me.mauricee.dreamscape.daydream

import android.os.Build
import android.util.DisplayMetrics
import me.mauricee.ext.logd
import javax.inject.Inject

class DisplayManager @Inject constructor(private val service: DayDreamService) {

    fun isHdrSupported() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        service.windowManager.defaultDisplay.isHdr
    } else {
        false
    }

    fun getDisplayType(): DisplayType {
        val displayMetrics = DisplayMetrics()
        val width: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            service.windowManager.defaultDisplay.supportedModes
                    .map { it.physicalHeight }
                    .maxBy { it } ?: 0

        } else {
            service.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }

        logd("width: $width")
        return when {
            width < 1080 -> DisplayType.SD
            width in 1080..2160 -> DisplayType.HD
            else -> DisplayType.UHD
        }
    }

    enum class DisplayType {
        UHD,
        HD,
        SD
    }
}