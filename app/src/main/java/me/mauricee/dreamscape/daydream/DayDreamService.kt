package me.mauricee.dreamscape.daydream

import android.content.Context
import android.os.Looper
import android.service.dreams.DreamService
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import dagger.android.AndroidInjection
import dagger.android.DaggerService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.view_dream.view.*
import me.mauricee.dreamscape.R
import me.mauricee.ext.logd
import javax.inject.Inject

class DayDreamService : DreamService() {

    @Inject
    lateinit var presenter: DreamPresenter

    private val subs = CompositeDisposable()
    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onDreamingStarted() {
        super.onDreamingStarted()
        isInteractive = false
        isFullscreen = true
        DreamView(this).run {
            setContentView(this)
            subs += presenter.attachView(this).observeOn(AndroidSchedulers.from(Looper.myLooper())).subscribe(this::updateState)
        }
    }

    override fun onDreamingStopped() {
        super.onDreamingStopped()
        subs.clear()
    }

    class DreamView : ConstraintLayout, DreamContract.View {
        private lateinit var player: ExoPlayer

        constructor(context: Context) : super(context)
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

        init {
            View.inflate(context, R.layout.view_dream, this)
        }

        fun updateState(state: DreamContract.State): Unit = when (state) {
            is DreamContract.State.UpdateWeather -> updateWeather(state)
            is DreamContract.State.BindPlayer -> {
                this.player = state.player
                dream_player.player = player
                player.repeatMode = Player.REPEAT_MODE_ALL
            }
            is DreamContract.State.Play -> {
                player.prepare(state.source)
                player.playWhenReady = true
            }
            is DreamContract.State.Location -> dream_weather_location.text = state.location
        }

        private fun updateWeather(state: DreamContract.State.UpdateWeather) {
            dream_weather_location.text = state.temp
            dream_weather_temp.text = state.temp
        }
    }
}