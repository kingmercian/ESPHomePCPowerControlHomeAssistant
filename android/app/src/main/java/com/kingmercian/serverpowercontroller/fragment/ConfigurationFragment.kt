package com.kingmercian.serverpowercontroller.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import com.kingmercian.ServerPowerController.R
import com.kingmercian.serverpowercontroller.constant.Constant
import com.kingmercian.serverpowercontroller.model.Storage


class ConfigurationFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        setPreferenceOnChange(Constant.IP_ADDRESS_ST0RAGE)
        setPreferenceOnChange(Constant.PORT_STORAGE)

        setPreferenceOnChange(Constant.USERNAME_STORAGE)
        setPreferenceOnChange(Constant.PASSWORD_STORAGE)
    }

    private fun setPreferenceOnChange(storage: Storage) {
        val (key, default) = storage
        val preference: EditTextPreference? = findPreference(key)
        preference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { pref ->
            val value = pref.text
            if (value.isNullOrBlank()) default else value
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }


}