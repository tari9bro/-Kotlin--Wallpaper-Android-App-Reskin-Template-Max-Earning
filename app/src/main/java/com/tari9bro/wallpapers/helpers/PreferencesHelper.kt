package com.tari9bro.wallpapers.helpers

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesHelper(var activity: Activity) {
    fun SaveString(key: String?, value: String?) {
        val preferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun LoadString(key: String?): String? {
        val preferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        return preferences.getString(key, activity.packageName)
    }

    fun SaveInt(key: String?, value: Int) {
        val preferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun LoadInt(key: String?): Int {
        val preferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        return preferences.getInt(key, 0)
    }

    fun LoadBool(key: String?): Boolean {
        val preferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        return preferences.getBoolean(key, false)
    }

    fun SaveBool(key: String?, value: Boolean?) {
        val preferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }

    fun saveIntArray(intNumb: Int, KEY_INT_ARRAY: String?) {
        val gson = Gson()
        val intArray = ArrayList<Int>()
        intArray.add(intNumb)
        val preferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        val intArrayJson = gson.toJson(intArray)
        editor.putString(KEY_INT_ARRAY, intArrayJson)
        editor.apply()
    }

    fun LoadIntArray(KEY_INT_ARRAY: String?): ArrayList<Int?> {
        val gson = Gson()
        val preferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        val intArrayJson = preferences.getString(KEY_INT_ARRAY, null)
        if (intArrayJson != null) {
            val type = object : TypeToken<ArrayList<Int?>?>() {}.type
            return gson.fromJson(intArrayJson, type)
        } else {
            return ArrayList()
        }
    }

    fun saveConstraintLayout(isConstraintLayoutVisible: Boolean) {
        val sharedPreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isConstraintLayoutVisible", isConstraintLayoutVisible)
        editor.apply()
    }

    fun loadConstraintLayout(): Boolean {
        val sharedPreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isConstraintLayoutVisible", false)
    }
}

