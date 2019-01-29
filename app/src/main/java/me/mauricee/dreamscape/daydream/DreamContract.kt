package me.mauricee.dreamscape.daydream

import com.google.android.exoplayer2.ExoPlayer
import me.mauricee.dreamscape.BaseContract

class DreamContract {
    interface View : BaseContract.View<Action>
    interface Presenter : BaseContract.Presenter<View, State, Action>
    class Action
    sealed class State {
        data class UpdateWeather(val temp: String, val icon: String) : State()
        data class BindPlayer(val player: ExoPlayer) : State()
    }
}