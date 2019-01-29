package me.mauricee.dreamscape.domain.apple

import com.google.gson.annotations.SerializedName

//https://sylvan.apple.com/Aerials/2x/entries.json
data class VideoEntry(val version: String, val assets: List<VideoAsset>) {
}

data class VideoAsset(@SerializedName("id")
                      val id: String,
                      @SerializedName("url-1080-SDR")
                      val url1080SDR: String,
                      @SerializedName("url-1080-HDR")
                      val url1080hdr: String,
                      @SerializedName("url-4K-SDR")
                      val url4Ksdr: String,
                      @SerializedName("url-4K-HDR")
                      val url4Khdr: String,
                      @SerializedName("accessibilityLabel")
                      val accessibilityLabel: String)