package com.tari9bro.wallpapers.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.helper.widget.MotionEffect
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.facebook.ads.RewardedVideoAd
import com.facebook.ads.RewardedVideoAdListener
import com.tari9bro.wallpapers.R
import com.tari9bro.wallpapers.helpers.PreferencesHelper

class Ads(private val activity: Activity, private val context: Context) {
    var pref: PreferencesHelper = PreferencesHelper(activity)
    var adView: AdView? = null


    var interstitialAd: InterstitialAd? = null


    fun loadBanner() {
        // Create an ad request.
// Instantiate an AdView object.
// NOTE: The placement ID from the Facebook Monetization Manager identifies your App.
// To get test ads, add IMG_16_9_APP_INSTALL# to your placement id. Remove this when your app is ready to serve real ads.

        adView = AdView(context, activity.getString(R.string.bannerAd), AdSize.BANNER_HEIGHT_50)
        // Find the Ad Container
        val adContainer = activity.findViewById<View>(R.id.banner_container) as LinearLayout
        // Add the ad view to your activity layout
        adContainer.addView(adView)
        // Request an ad
        adView!!.loadAd()
    }

    fun LoadInterstitialAd() {
        // Instantiate an InterstitialAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        interstitialAd = InterstitialAd(context, activity.getString(R.string.bannerAd))
        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Interstitial ad displayed callback
                Log.e(MotionEffect.TAG, "Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad) {
                // Interstitial dismissed callback
                Log.e(MotionEffect.TAG, "Interstitial ad dismissed.")
            }

            override fun onError(ad: Ad, adError: AdError) {
                // Ad error callback
                Log.e(MotionEffect.TAG, "Interstitial ad failed to load: " + adError.errorMessage)
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(MotionEffect.TAG, "Interstitial ad is loaded and ready to be displayed!")
                // Show the ad
                interstitialAd!!.show()
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
                Log.d(MotionEffect.TAG, "Interstitial ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
                Log.d(MotionEffect.TAG, "Interstitial ad impression logged!")
            }
        }

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd!!.loadAd(
            interstitialAd!!.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build()
        )
    }

    fun loadRewarded() {
        rewardedAd = RewardedVideoAd(context, activity.getString(R.string.bannerAd))
        val rewardedVideoAdListener: RewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onError(ad: Ad, error: AdError) {
                // Rewarded video ad failed to load
                Log.e(MotionEffect.TAG, "Rewarded video ad failed to load: " + error.errorMessage)
            }

            override fun onAdLoaded(ad: Ad) {
                // Rewarded video ad is loaded and ready to be displayed
                Log.d(MotionEffect.TAG, "Rewarded video ad is loaded and ready to be displayed!")
            }

            override fun onAdClicked(ad: Ad) {
                // Rewarded video ad clicked
                Log.d(MotionEffect.TAG, "Rewarded video ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d(MotionEffect.TAG, "Rewarded video ad impression logged!")
            }

            override fun onRewardedVideoCompleted() {
                // Rewarded Video View Complete - the video has been played to the end.
                // You can use this event to initialize your reward
                Log.d(MotionEffect.TAG, "Rewarded video completed!")
                pref.saveIntArray(positionInt, "unlockedList")
                pref.SaveBool("Rewarded", true)
                // Call method to give reward
                // giveReward();
            }

            override fun onRewardedVideoClosed() {
                // The Rewarded Video ad was closed - this can occur during the video
                // by closing the app, or closing the end card.
                Log.d(MotionEffect.TAG, "Rewarded video ad closed!")
            }
        }
        rewardedAd!!.loadAd(
            rewardedAd!!.buildLoadAdConfig()
                .withAdListener(rewardedVideoAdListener)
                .build()
        )
    }

    fun playInterstitialAd() {
        if (interstitialAd != null && !interstitialAd!!.isAdInvalidated) {
            interstitialAd!!.show()
        } else {
            Toast.makeText(context, "ad is not ready yet!", Toast.LENGTH_LONG).show()
        }
    }


    fun playRewarded() {
        if (rewardedAd != null && !rewardedAd!!.isAdInvalidated) {
            rewardedAd!!.show()
        } else {
            Toast.makeText(context, "ad is not ready yet!", Toast.LENGTH_LONG).show()
        }
    }


    companion object {
        var rewardedAd: RewardedVideoAd? = null


        var positionInt: Int = 0
    }
}
