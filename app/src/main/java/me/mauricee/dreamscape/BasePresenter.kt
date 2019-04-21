package me.mauricee.dreamscape

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class BasePresenter<V : BaseContract.View<A>, S, A> : BaseContract.Presenter<V, S, A> {

    final override fun attachView(view: V): Observable<S> = onViewAttached(view)
            .doOnDispose(::onViewDetached)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())

    inline fun stateless(crossinline action: () -> Unit): Observable<S> =
            Observable.defer { action(); Observable.empty<S>() }

    internal open fun onViewAttached(view: V): Observable<S> = Observable.empty()

    internal open fun onViewDetached() {}
}