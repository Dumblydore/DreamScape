package me.mauricee.dreamscape.daydream

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.Module
import dagger.Provides
import me.mauricee.dreamscape.BuildConfig
import javax.inject.Scope

@Module
abstract class DreamModule {

    @Module
    companion object {


        @Provides
        @JvmStatic
        @DreamScope
        fun providesExoPlayer(context: Context) = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
                .apply {
                    videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                }

        @Provides
        @JvmStatic
        @DreamScope
        fun providersExtractor(context: Context) = ExtractorMediaSource.Factory(DefaultDataSourceFactory(context, BuildConfig.APPLICATION_ID))
    }
}

@Scope
@MustBeDocumented
annotation class DreamScope