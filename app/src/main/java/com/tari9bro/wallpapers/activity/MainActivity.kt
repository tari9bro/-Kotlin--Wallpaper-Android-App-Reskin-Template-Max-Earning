package com.tari9bro.wallpapers.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.facebook.ads.AdSettings
import com.facebook.ads.AdSettings.IntegrationErrorMode
import com.facebook.ads.AudienceNetworkAds
import com.tari9bro.wallpapers.R
import com.tari9bro.wallpapers.ads.Ads
import com.tari9bro.wallpapers.fragments.RecyclerFragment
import com.tari9bro.wallpapers.helpers.ClickListenerHelper
import com.tari9bro.wallpapers.helpers.PreferencesHelper
import com.tari9bro.wallpapers.helpers.Settings
import com.tari9bro.wallpapers.notification.NotificationHelper


class MainActivity : AppCompatActivity() {
    var ads: Ads? = null
    var prefs: PreferencesHelper? = null
    var settings: Settings? = null
    var clickListenerHelper: ClickListenerHelper? = null

    var notificationHelper: NotificationHelper? = null

    @RequiresApi(api = Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)


        Companion.fragmentManager = supportFragmentManager
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RecyclerFragment())
                .commit()
        }
        settings = Settings(this@MainActivity, this)
        prefs = PreferencesHelper(this)

        if (!prefs!!.LoadBool("Permission")) {
            settings!!.showPermissionDialog()
        }

        AudienceNetworkAds.initialize(this)
        AdSettings.setIntegrationErrorMode(IntegrationErrorMode.INTEGRATION_ERROR_CRASH_DEBUG_MODE)
        AdSettings.addTestDevice("65f07fc3-1f8c-4a08-815a-49bc93c63a54")
        AdSettings.addTestDevice("cd683b7d-35e6-4ffb-bd94-10ca1ad1abe1")
        AdSettings.addTestDevice("bd2c454d-e580-4633-ada2-dba7898b33dd")


        clickListenerHelper = ClickListenerHelper(this@MainActivity, this)

        findViewById<View>(R.id.share).setOnClickListener(clickListenerHelper)
        findViewById<View>(R.id.apps).setOnClickListener(clickListenerHelper)
        findViewById<View>(R.id.exit).setOnClickListener(clickListenerHelper)
        findViewById<View>(R.id.floatingActionButton).setOnClickListener(clickListenerHelper)
        findViewById<View>(R.id.rating).setOnClickListener(clickListenerHelper)
        findViewById<View>(R.id.Feedback).setOnClickListener(clickListenerHelper)
        notificationHelper = NotificationHelper(this)
        NotificationHelper.createNotificationChannel(this)
        notificationHelper!!.startPeriodicTasks()

        ads = Ads(this@MainActivity, this)

        ads!!.loadBanner()
        ads!!.loadRewarded()
        ads!!.LoadInterstitialAd()

        settings!!.noInternetDialog(lifecycle)
    }


    override fun onDestroy() {
        if (ads!!.adView != null) {
            ads!!.adView.destroy()
        }
        if (ads!!.interstitialAd != null) {
            ads!!.interstitialAd.destroy()
        }
        if (Ads.rewardedAd != null) {
            Ads.rewardedAd.destroy()
            Ads.rewardedAd = null
        }
        super.onDestroy()
    }

    companion object {
        @JvmField
        var fragmentManager: FragmentManager? = null
    }
}
