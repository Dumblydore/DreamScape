package me.mauricee.dreamscape.daydream

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import me.mauricee.dreamscape.BaseContract

class DreamContract {
    interface View : BaseContract.View<Action>
    interface Presenter : BaseContract.Presenter<View, State, Action>
    class Action
    sealed class State {
        data class UpdateWeather(val temp: String, val icon: String) : State()
        data class Play(val source: MediaSource) : State()
        data class Location(val location: String) : State()
        data class BindPlayer(val player: SimpleExoPlayer) : State()
    }
}