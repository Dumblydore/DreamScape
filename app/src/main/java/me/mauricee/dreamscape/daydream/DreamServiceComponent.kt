package me.mauricee.dreamscape.daydream

import dagger.Subcomponent
import dagger.android.AndroidInjector

@DreamScope
@Subcomponent(modules = arrayOf(DreamModule::class))
interface DreamServiceComponent : AndroidInjector<DayDreamService> {


    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<DayDreamService>()
}