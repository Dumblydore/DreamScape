package me.mauricee.dreamscape.daydream

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import me.mauricee.dreamscape.BasePresenter
import me.mauricee.dreamscape.BuildConfig
import me.mauricee.dreamscape.domain.apple.AppleApi
import me.mauricee.dreamscape.domain.apple.VideoAsset
import me.mauricee.dreamscape.domain.exo.positionDicontinuity
import me.mauricee.dreamscape.domain.location.LocationHelper
import me.mauricee.dreamscape.domain.weather.WeatherService
import me.mauricee.ext.logd
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class DreamPresenter @Inject constructor(private val exoPlayer: SimpleExoPlayer,
                                         private val sourceFactory: ExtractorMediaSource.Factory,
                                         private val displayManager: DisplayManager,
                                         private val weatherService: WeatherService,
                                         private val locationHelper: LocationHelper,
                                         private val appleApi: AppleApi) : DreamContract.Presenter,
        BasePresenter<DreamContract.View, DreamContract.State, DreamContract.Action>() {

    override fun onViewAttached(view: DreamContract.View): Observable<DreamContract.State> =
            Observable.merge(appleApi.arials.flatMap { Observable.merge(preparePlayer(it.assets), getLocations(it.assets)) }, getWeather())
                    .startWith(DreamContract.State.BindPlayer(exoPlayer))

    private fun preparePlayer(arials: List<VideoAsset>) = arials.toObservable()
            .map(this::determineBestStream).map(this::buildMediaSource)
            .toList().map { it.apply { shuffle() } }.map { ConcatenatingMediaSource(*it.toTypedArray()) }
            .map<DreamContract.State> { DreamContract.State.Play(it) }.toObservable()

    private fun getLocations(arials: List<VideoAsset>) = exoPlayer.positionDicontinuity().map { arials[exoPlayer.currentWindowIndex] }
            .startWith(arials.first())
            .map { DreamContract.State.Location(it.accessibilityLabel) }


    private fun buildMediaSource(uri: Uri): MediaSource = sourceFactory.createMediaSource(uri)

    private fun determineBestStream(videoAsset: VideoAsset): Uri = (if (displayManager.isHdrSupported()) {
        when (displayManager.getDisplayType()) {
            DisplayManager.DisplayType.UHD -> videoAsset.url4Khdr
            else -> videoAsset.url1080hdr
        }
    } else {
        when (displayManager.getDisplayType()) {
            DisplayManager.DisplayType.UHD -> videoAsset.url4Ksdr
            else -> videoAsset.url4Ksdr
        }
    }).let { it.replace("https", "http") }.toUri()

    private fun getWeather(): Observable<DreamContract.State> = locationHelper.getLocation()
            .flatMapObservable { loc -> Observable.interval(1, TimeUnit.HOURS).map { loc } }
            .flatMapSingle { weatherService.getForecast(BuildConfig.WEATHER_API, it.long, it.lat) }
            .map { DreamContract.State.UpdateWeather("${it.currently.temperature}°", "") }

    override fun onViewDetached() {
        exoPlayer.release()
    }
}