package me.mauricee.dreamscape.daydream

import android.content.Context
import android.location.LocationManager
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.extractor.ts.TsExtractor
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.TimestampAdjuster
import dagger.Binds
import dagger.Module
import dagger.Provides
import me.mauricee.dreamscape.BuildConfig
import me.mauricee.dreamscape.di.AppScope
import okhttp3.Call
import okhttp3.OkHttpClient
import javax.inject.Scope

@Module
abstract class DreamModule {

    @Binds
    abstract fun bindContext(service: DayDreamService): Context

    @Module
    companion object {


        @Provides
        @JvmStatic
        @DreamScope
        fun providesExoPlayer(context: Context): SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
                .apply {
                    videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                }

        @Provides
        @JvmStatic
        @DreamScope
        fun providesDataSource(client: OkHttpClient) = OkHttpDataSourceFactory(client::newCall, BuildConfig.APPLICATION_ID)

        @Provides
        @JvmStatic
        @DreamScope
        fun providersExtractor(source: OkHttpDataSourceFactory) : ExtractorMediaSource.Factory{
//            val extractor = TsExtractor(DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES)
            return ExtractorMediaSource.Factory(source)
//                    .apply { this.setExtractorsFactory(ExtractorsFactory { arrayOf(extractor) }) }
        }

        @Provides
        @JvmStatic
        @DreamScope
        fun providesLocationService(context: Context): LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        @Provides
        @JvmStatic
        @DreamScope
        fun providersDashExtractor(source: OkHttpDataSourceFactory) = DashMediaSource.Factory(source)

        @Provides
        @JvmStatic
        @DreamScope
        fun providersHlsExtractor(source: OkHttpDataSourceFactory) = HlsMediaSource.Factory(source)
    }
}

@Scope
@MustBeDocumented
annotation class DreamScope