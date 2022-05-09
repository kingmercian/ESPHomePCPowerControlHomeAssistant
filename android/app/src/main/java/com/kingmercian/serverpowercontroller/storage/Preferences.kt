package com.kingmercian.serverpowercontroller.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.kingmercian.serverpowercontroller.constant.Constant
import com.kingmercian.serverpowercontroller.model.Storage

class Preferences(ctx: Context) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)
    private val editor: SharedPreferences.Editor = preferences.edit()

    private fun getSharedPreferencesValue(storage: Storage): String {
        val (key, default) = storage
        val value = preferences.getString(key, default)!!
        return if (value.isBlank()) default else value
    }

    fun getIpAddress(): String = getSharedPreferencesValue(Constant.IP_ADDRESS_ST0RAGE)
    fun getPort(): String = getSharedPreferencesValue(Constant.PORT_STORAGE)

    fun getUsername(): String = getSharedPreferencesValue(Constant.USERNAME_STORAGE)
    fun getPassword(): String = getSharedPreferencesValue(Constant.PASSWORD_STORAGE)

}



