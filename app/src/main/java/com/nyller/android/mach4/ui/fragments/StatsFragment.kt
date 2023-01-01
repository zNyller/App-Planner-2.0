package com.nyller.android.mach4.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nyller.android.mach4.R
import com.nyller.android.mach4.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {

    private lateinit var binding : FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)

        Log.i("Edu", "onCreateView: Stats Fragment")

        return binding.root
    }

}