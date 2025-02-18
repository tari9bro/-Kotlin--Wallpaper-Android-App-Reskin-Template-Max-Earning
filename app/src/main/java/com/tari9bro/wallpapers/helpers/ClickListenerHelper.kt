package com.tari9bro.wallpapers.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import com.tari9bro.wallpapers.R

class ClickListenerHelper(private val activity: Activity, context: Context) : View.OnClickListener {
    private val settings = Settings(activity, context)
    private val prefs = PreferencesHelper(activity)

    @SuppressLint("NonConstantResourceId")
    override fun onClick(view: View) {
        // Handle onClick event for all TextViews here
        if (view.id == R.id.share) {
            settings.sharTheApp()
            showConstraintLayout()
        }
        if (view.id == R.id.apps) {
            settings.moreApps()
            showConstraintLayout()
        }
        if (view.id == R.id.exit) {
            settings.exitTheApp()
            showConstraintLayout()
        }

        if (view.id == R.id.floatingActionButton) {
            showConstraintLayout()
        }
        if (view.id == R.id.rating) {
            settings.moreApps()
            showConstraintLayout()
        }
        if (view.id == R.id.Feedback) {
            settings.moreApps()
            showConstraintLayout()
        }
    }

    private fun showConstraintLayout() {
        if (!prefs.loadConstraintLayout()) {
            // Show ConstraintLayout
            activity.findViewById<View>(R.id.fabLayout).visibility = View.VISIBLE
            activity.findViewById<View>(R.id.floatingActionButton).animate().rotation(45f)
            prefs.saveConstraintLayout(true)
        } else {
            // Hide ConstraintLayout
            // inter.playInterstitialAd();
            activity.findViewById<View>(R.id.fabLayout).visibility = View.GONE
            activity.findViewById<View>(R.id.floatingActionButton).animate().rotation(0f)
            prefs.saveConstraintLayout(false)
        }
    }
}
