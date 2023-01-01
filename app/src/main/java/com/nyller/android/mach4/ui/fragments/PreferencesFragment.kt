package com.nyller.android.mach4.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.nyller.android.mach4.R

class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_app, rootKey)
    }
}