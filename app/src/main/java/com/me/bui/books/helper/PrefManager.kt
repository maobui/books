package com.me.bui.books.helper

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by mao.buidinh on 7/17/2017.
 */

class PrefManager(context: Context) {
    private val sharedPreferences: SharedPreferences

    var preLoad: Boolean
        get() = sharedPreferences.getBoolean(PRE_LOAD, false)
        set(isPreLoad) = sharedPreferences
                .edit()
                .putBoolean(PRE_LOAD, isPreLoad)
                .apply()

    init {

        sharedPreferences = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {

        private val PRE_LOAD = "preLoad"
        private val PREFS_NAME = "prefs"
        private var instance: PrefManager? = null

        fun with(context: Context): PrefManager {

            if (instance == null) {
                instance = PrefManager(context)
            }
            return instance as PrefManager
        }
    }

}
