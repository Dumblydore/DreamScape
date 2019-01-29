package me.mauricee.dreamscape

import io.reactivex.Observable

interface BaseContract {

    interface View<A> {
        val actions: Observable<A>
            get() = Observable.empty<A>()
    }

    interface Presenter<in V : View<A>, S, A> {
        fun attachView(view: V) : Observable<S>
    }
}