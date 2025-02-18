package com.tari9bro.wallpapers.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tari9bro.wallpapers.R
import com.tari9bro.wallpapers.activity.MainActivity
import com.tari9bro.wallpapers.adapters.FileAdapter2
import com.tari9bro.wallpapers.ads.Ads
import com.tari9bro.wallpapers.helpers.FileUtilsHelper
import com.tari9bro.wallpapers.helpers.PreferencesHelper
import com.tari9bro.wallpapers.helpers.Settings
import com.tari9bro.wallpapers.helpers.WallpaperSetter
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import java.util.concurrent.atomic.AtomicInteger


class ImageFragment : Fragment() {
    var ads: Ads? = null
    var pref: PreferencesHelper? = null
    var share: TextView? = null
    var Download: TextView? = null
    var setWall: TextView? = null
    var badge: TextView? = null
    var wallpaperSetter: WallpaperSetter? = null
    var settings: Settings? = null
    private val synchronizedPosition = AtomicInteger(0)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_image, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)


        wallpaperSetter = WallpaperSetter(requireContext())





        settings = Settings(requireActivity(), requireContext())
        pref = PreferencesHelper(requireActivity())
        ads = Ads(requireActivity(), requireContext())

        val layoutManager = GridLayoutManager(
            requireContext(),
            1
        ) // or GridLayoutManager if you are using a grid layout
        recyclerView.layoutManager = layoutManager


        val fileList = FileUtilsHelper.getFilesFromAssets(requireContext())
        val fileAdapter = FileAdapter2(
            requireContext(),
            fileList,
            requireActivity(),
            MainActivity.fragmentManager
        ) // Adjust the placeholder dimensions as per your preference
        recyclerView.adapter = fileAdapter


        val alphaInAnimationAdapter = AlphaInAnimationAdapter(fileAdapter)
        alphaInAnimationAdapter.duration = 1000
        alphaInAnimationAdapter.interpolator = OvershootInterpolator()
        alphaInAnimationAdapter.isFirstOnly = false
        recyclerView.adapter = ScaleInAnimationAdapter(alphaInAnimationAdapter)

        layoutManager.scrollToPosition(pref!!.LoadInt("position"))


        // Add an OnScrollListener to the RecyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Get the first visible item position
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val firstVisibleItemPosition = layoutManager!!.findFirstVisibleItemPosition()

                // Update the synchronized position
                synchronizedPosition.set(firstVisibleItemPosition)

                if (!containsDigitThree(synchronizedPosition.get())) {
                    badge!!.visibility = View.GONE
                    Download!!.visibility = View.VISIBLE
                    setWall!!.visibility = View.VISIBLE
                    share!!.visibility = View.VISIBLE
                } else {
                    badge!!.visibility = View.VISIBLE
                    if (!pref!!.LoadIntArray("unlockedList").contains(synchronizedPosition.get())) {
                        Download!!.visibility = View.GONE
                        setWall!!.visibility = View.GONE
                        share!!.visibility = View.GONE


                        // Assuming you're inside the fragment class
                        if (badge!!.parent != null) {
                            // Remove the view from its current parent
                            (badge!!.parent as ViewGroup).removeView(badge)
                            badge!!.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT // You can set the height as needed
                            )
                            badge!!.gravity = Gravity.CENTER

                            val container = view!!.findViewById<ViewGroup>(R.id.relativ)
                            container.addView(badge)
                        }
                    } else {
                        Download!!.visibility = View.VISIBLE
                        setWall!!.visibility = View.VISIBLE
                        share!!.visibility = View.GONE
                    }
                }
            }
        })


        share = rootView.findViewById(R.id.share)
        badge = rootView.findViewById(R.id.badge)
        setWall = rootView.findViewById(R.id.setwall)
        Download = rootView.findViewById(R.id.Download)

        share.setOnClickListener(View.OnClickListener { view: View? -> settings!!.sharTheApp() })
        badge.setOnClickListener(View.OnClickListener { view: View? ->
            if (containsDigitThree(synchronizedPosition.get()) && !pref!!.LoadIntArray("unlockedList")
                    .contains(synchronizedPosition.get())
            ) {
                if (Ads.Companion.rewardedAd != null && !Ads.Companion.rewardedAd.isAdInvalidated()) {
                    ShowDialog(fileList!![synchronizedPosition.get()])
                } else {
                    Toast.makeText(requireContext(), "not ready, try later", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                wallpaperSetter!!.showWallpaperDialog(fileList!![synchronizedPosition.get()])
            }
        })
        setWall.setOnClickListener(View.OnClickListener { view: View? ->
            wallpaperSetter!!.showWallpaperDialog(
                fileList!![synchronizedPosition.get()]
            )
        })
        Download.setOnClickListener(View.OnClickListener { view: View? ->
            settings!!.saveImage(
                fileList!![synchronizedPosition.get()]
            )
        })


        // Add this block for badge image
        return rootView
    }

    private fun ShowDialog(fileName: String?) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle(R.string.exit_dialog_title)
        dialog.setIcon(R.drawable.ic_exit)
        dialog.setMessage(R.string.rewarded_dialog)
        dialog.setCancelable(false)
        dialog.setPositiveButton(R.string.yes) { dialogInterface: DialogInterface?, i: Int ->
            if (pref!!.LoadBool("Rewarded")) {
                pref!!.SaveBool("Rewarded", false)

                val wallpaperSetter = WallpaperSetter(requireContext())
                wallpaperSetter.showWallpaperDialog(fileName)
            } else {
                ads!!.playRewarded()
            }
        }

        dialog.setNegativeButton(R.string.no) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
        dialog.show()
    }

    companion object {
        private const val ARG_FILE_NAME = "fileName"
        private const val ARG_BADGE_RES_ID = "badgeResId" // Add this for badge image

        fun newInstance(fileName: String?): ImageFragment {
            val fragment = ImageFragment()
            val args = Bundle()
            args.putString(ARG_FILE_NAME, fileName)
            fragment.arguments = args
            return fragment
        }

        // Add this method for badge image
        fun newInstanceWithBadge(fileName: String?, badgeResId: Int): ImageFragment {
            val fragment = ImageFragment()
            val args = Bundle()
            args.putString(ARG_FILE_NAME, fileName)
            args.putInt(ARG_BADGE_RES_ID, badgeResId)
            fragment.arguments = args
            return fragment
        }

        fun containsDigitThree(number: Int): Boolean {
            val numberString = number.toString()
            return numberString.contains("3")
        }
    }
}