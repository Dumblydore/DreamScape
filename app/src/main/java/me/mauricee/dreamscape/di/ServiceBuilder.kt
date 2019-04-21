package me.mauricee.dreamscape.di

import android.app.Service
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap
import me.mauricee.dreamscape.daydream.DayDreamService
import me.mauricee.dreamscape.daydream.DreamServiceComponent


@Module
abstract class ServiceBuilder {

    @Binds
    @IntoMap
    @ServiceKey(DayDreamService::class)
    internal abstract fun bindService(builder: DreamServiceComponent.Builder): AndroidInjector.Factory<out Service>
}