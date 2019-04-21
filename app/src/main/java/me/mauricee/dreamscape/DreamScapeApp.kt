package me.mauricee.dreamscape

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import me.mauricee.dreamscape.di.DaggerAppComponent

class DreamScapeApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerAppComponent.builder().application(this).build()
            .also { it.inject(this) }
}