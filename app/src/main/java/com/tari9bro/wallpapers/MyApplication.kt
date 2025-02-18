package com.tari9bro.wallpapers

import android.app.Application
import com.facebook.ads.AudienceNetworkAds

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AudienceNetworkAds.initialize(this)
    }
}

