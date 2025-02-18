package com.tari9bro.wallpapers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tari9bro.wallpapers.R
import com.tari9bro.wallpapers.adapters.FileAdapter
import com.tari9bro.wallpapers.helpers.FileUtilsHelper
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter


class RecyclerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_recycler, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2) // Display 2 items per row

        val fileList = FileUtilsHelper.getFilesFromAssets(requireContext())
        val fileAdapter = FileAdapter(
            requireContext(),
            fileList,
            150,
            350,
            requireActivity()
        ) // Adjust the placeholder dimensions as per your preference
        recyclerView.adapter = fileAdapter
        val alphaInAnimationAdapter = AlphaInAnimationAdapter(fileAdapter)
        alphaInAnimationAdapter.duration = 1000
        alphaInAnimationAdapter.interpolator = OvershootInterpolator()
        alphaInAnimationAdapter.isFirstOnly = false
        recyclerView.adapter = ScaleInAnimationAdapter(alphaInAnimationAdapter)
        return rootView
    }
}
