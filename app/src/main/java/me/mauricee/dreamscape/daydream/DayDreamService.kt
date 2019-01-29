package me.mauricee.dreamscape.daydream

import android.content.Context
import android.service.dreams.DreamService
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.view_dream.view.*
import me.mauricee.dreamscape.R
import javax.inject.Inject

class DayDreamService : DreamService() {

    @Inject
    lateinit var presenter: DreamPresenter

    private val subs = CompositeDisposable()

    override fun onDreamingStarted() {
        super.onDreamingStarted()
        DreamView(this).run {
            setContentView(this)
            subs += presenter.attachView(this).subscribe(this::updateState)
        }
    }

    override fun onDreamingStopped() {
        super.onDreamingStopped()
        subs.clear()
    }

    class DreamView : ConstraintLayout, DreamContract.View {

        constructor(context: Context) : super(context)
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

        init {
            View.inflate(context, R.layout.view_dream, this)
        }

        fun updateState(state: DreamContract.State): Unit = when (state) {
            is DreamContract.State.UpdateWeather -> updateWeather(state)
            is DreamContract.State.BindPlayer -> dream_player.player = state.player
        }

        private fun updateWeather(state: DreamContract.State.UpdateWeather) {
            dream_weather_icon
            dream_weather_location.text = state.temp
            dream_weather_temp.text = state.temp
        }
    }
}