package com.charityright.bd.Utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object CustomSharedPref {
    private var mSharedPref: SharedPreferences? = null
    val NAME = "com.charityright.charityauthority"

    fun init(context: Context) {
        if (mSharedPref == null){
            mSharedPref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        }
    }

    fun read(key: String?, defValue: String?): String? {
        return mSharedPref!!.getString(key, defValue)
    }

    fun write(key: String?, value: String?) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun saveArrayList(key: String?,list: ArrayList<String?>?) {
        val prefsEditor = mSharedPref!!.edit()
        val gson = Gson()
        val json: String = gson.toJson(list)
        prefsEditor.putString(key, json)
        prefsEditor.apply()
    }

    fun getArrayList(key: String?,defValue: String?): ArrayList<String?>? {
        val gson = Gson()
        val json: String? = mSharedPref!!.getString(key, defValue)
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(json, type)
    }
}