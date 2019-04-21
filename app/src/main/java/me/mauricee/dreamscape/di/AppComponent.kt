package me.mauricee.dreamscape.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import me.mauricee.dreamscape.DreamScapeApp

@AppScope
@Component(modules = [AppModule::class, ServiceBuilder::class, AndroidSupportInjectionModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(application: DreamScapeApp)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}