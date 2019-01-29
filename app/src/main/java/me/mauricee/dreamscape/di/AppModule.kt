package me.mauricee.dreamscape.di

import android.service.dreams.DreamService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import me.mauricee.dreamscape.daydream.DreamModule
import me.mauricee.dreamscape.daydream.DreamScope
import me.mauricee.dreamscape.domain.apple.AppleApi
import okhttp3.OkHttpClient
import org.aaronhe.threetengson.ThreeTenGsonAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Scope

@Module
abstract class AppModule() {

    @DreamScope
    @ContributesAndroidInjector(modules = [DreamModule::class])
    abstract fun contributesDreamService(): DreamService


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
                .baseUrl("https://www.floatplane.com/api/")
                .build().create(AppleApi::class.java)

    }
}

@Scope
@MustBeDocumented
annotation class AppScope