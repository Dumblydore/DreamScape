package me.mauricee.dreamscape.domain.exo

import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class ExoPlayerEventObservable(private val player: ExoPlayer) : Observable<ExoPlayerEvent>() {
    override fun subscribeActual(observer: Observer<in ExoPlayerEvent>) = observer.onSubscribe(Listener(player, observer).also(player::addListener))

    private class Listener(private val player: ExoPlayer, private val observer: Observer<in ExoPlayerEvent>) : Player.EventListener, MainThreadDisposable() {

        override fun onDispose() {
            player.removeListener(this)
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEvent.PlaybackParametersChanged(playbackParameters))
            }
        }

        override fun onSeekProcessed() {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEvent.SeekProcessed)
            }
        }

        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEvent.TracksChanged(trackGroups, trackSelections))
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEvent.PlayerError(error))
            }
        }

        override fun onLoadingChanged(isLoading: Boolean) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEvent.LoadingChanged(isLoading))
            }
        }

        override fun onPositionDiscontinuity(reason: Int) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEvent.PositionDiscontinuity(reason))
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEvent.RepeatModeChanged(repeatMode))
            }
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEvent.ShuffleModeEnabledChanged(shuffleModeEnabled))
            }
        }

        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEvent.TimelineChanged(timeline,manifest,reason))
            }
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEvent.PlayerStateChanged(playWhenReady,playbackState))
            }
        }
    }
}

fun ExoPlayer.positionDicontinuity() = ExoPlayerEventObservable(this)
        .filter { it is ExoPlayerEvent.PositionDiscontinuity }
        .cast(ExoPlayerEvent.PositionDiscontinuity::class.java)
        .subscribeOn(AndroidSchedulers.mainThread())
