package me.mauricee.dreamscape.di

import android.app.Service
import android.content.Context
import android.location.LocationManager
import android.service.dreams.DreamService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap
import me.mauricee.dreamscape.DreamScapeApp
import me.mauricee.dreamscape.daydream.DayDreamService
import me.mauricee.dreamscape.daydream.DreamModule
import me.mauricee.dreamscape.daydream.DreamScope
import me.mauricee.dreamscape.daydream.DreamServiceComponent
import me.mauricee.dreamscape.domain.apple.AppleApi
import me.mauricee.dreamscape.domain.weather.WeatherService
import okhttp3.OkHttpClient
import org.aaronhe.threetengson.ThreeTenGsonAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Scope

@Module(subcomponents = [DreamServiceComponent::class])
abstract class AppModule {

    @Module
    companion object {

        @AppScope
        @Provides
        @JvmStatic
        fun providesGson(): Gson = ThreeTenGsonAdapter.registerAll(GsonBuilder().setLenient()).create()

        @AppScope
        @Provides
        @JvmStatic
        fun providesRxJavaCallAdapterFactory(): RxJava2CallAdapterFactory =
                RxJava2CallAdapterFactory.createAsync()

        @AppScope
        @Provides
        @JvmStatic
        fun providesGsonConverterFactory(gson: Gson): GsonConverterFactory =
                GsonConverterFactory.create(gson)

        @AppScope
        @Provides
        @JvmStatic
        fun providesHttpClient(): OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

        @Provides
        @AppScope
        @JvmStatic
        fun providesAppleApi(converterFactory: GsonConverterFactory,
                             callFactory: RxJava2CallAdapterFactory, client: OkHttpClient):
                AppleApi = Retrofit.Builder().client(client)
                .addConverterFactory(converterFactory)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(callFactory)
                .baseUrl("http://sylvan.apple.com/")
                .build().create(AppleApi::class.java)


        @Provides
        @AppScope
        @JvmStatic
        fun providesWeatherService(converterFactory: GsonConverterFactory,
                             callFactory: RxJava2CallAdapterFactory, client: OkHttpClient):
                WeatherService = Retrofit.Builder().client(client)
                .addConverterFactory(converterFactory)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(callFactory)
                .baseUrl("https://api.darksky.net/")
                .build().create(WeatherService::class.java)

    }
}

@Scope
@MustBeDocumented
annotation class AppScope