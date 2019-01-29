package me.mauricee.dreamscape.domain.apple

import io.reactivex.Observable
import retrofit2.http.GET

interface AppleApi {
    @get:GET("Aerials/2x/entries.json")
    val arials: Observable<VideoEntry>
}