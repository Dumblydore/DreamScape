package me.mauricee.dreamscape.daydream

import android.net.Uri
import androidx.core.net.toUri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import me.mauricee.dreamscape.BasePresenter
import me.mauricee.dreamscape.domain.apple.AppleApi
import javax.inject.Inject


class DreamPresenter @Inject constructor(private val exoPlayer: ExoPlayer,
                                         private val sourceFactory: ExtractorMediaSource.Factory,
                                         private val appleApi: AppleApi) : DreamContract.Presenter,
        BasePresenter<DreamContract.View, DreamContract.State, DreamContract.Action>() {

    override fun onViewAttached(view: DreamContract.View): Observable<DreamContract.State> =
            Observable.merge(preparePlayer(), preparePlayer()).startWith(DreamContract.State.BindPlayer(exoPlayer))

    private fun preparePlayer() = appleApi.arials
            .flatMap { it.assets.toObservable() }.map { it.url4Khdr.toUri() }.map(this::buildMediaSource)
            .toList().map { it.apply { shuffle() } }.map { ConcatenatingMediaSource(*it.toTypedArray()) }
            .flatMapObservable { stateless { exoPlayer.prepare(it) } }
            .onErrorResumeNext(stateless {  })

    private fun buildMediaSource(uri: Uri): MediaSource = sourceFactory.createMediaSource(uri)

    override fun onViewDetached() {
        exoPlayer.release()
    }
}