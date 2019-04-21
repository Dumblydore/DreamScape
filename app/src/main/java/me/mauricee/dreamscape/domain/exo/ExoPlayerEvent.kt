package me.mauricee.dreamscape.domain.exo

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

sealed class ExoPlayerEvent {
    data class PlaybackParametersChanged(val playbackParameters: PlaybackParameters) : ExoPlayerEvent()
    object SeekProcessed : ExoPlayerEvent()
    data class TracksChanged(val trackGroups: TrackGroupArray, val trackSelections: TrackSelectionArray) : ExoPlayerEvent()
    data class PlayerError(val error: ExoPlaybackException) : ExoPlayerEvent()
    data class LoadingChanged(val isLoading: Boolean) : ExoPlayerEvent()
    data class PositionDiscontinuity(val reason: Int) : ExoPlayerEvent()
    data class RepeatModeChanged(val repeatMode: Int) : ExoPlayerEvent()
    data class ShuffleModeEnabledChanged(val shuffleModeEnabled: Boolean) : ExoPlayerEvent()
    data class TimelineChanged(val timeline: Timeline?, val manifest: Any?, val reason: Int) : ExoPlayerEvent()
    data class PlayerStateChanged(val playWhenReady: Boolean, val playbackState: Int) : ExoPlayerEvent()
}