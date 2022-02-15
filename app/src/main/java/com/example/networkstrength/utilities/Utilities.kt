package com.example.networkstrength.utilities


import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.lang.Exception

class Utilities {

    companion object{
        fun isNetworkConnected(activity: Context): Boolean{

            var isConnected = false
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                isConnected = when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    activeNetworkInfo?.run {
                        isConnected = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }
                    }
                }
            }

            return isConnected;
        }

        fun getNetworkType(activity: Context): Int{

            var type = -1
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return -1
                val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return -1
                type = when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> 0
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> 1
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> 2
                    else -> -1
                }
            } else {
                connectivityManager.run {
                    activeNetworkInfo?.run {
                        type = when (type) {
                            ConnectivityManager.TYPE_WIFI -> 0
                            ConnectivityManager.TYPE_MOBILE -> 1
                            ConnectivityManager.TYPE_ETHERNET -> 2
                            else -> -1
                        }
                    }
                }
            }

            return type
        }

        fun isNetworkReachAble(): Boolean{
            try {
                val p1 =
                    Runtime.getRuntime().exec("ping -c 1 www.google.com")
                val returnVal = p1.waitFor()
                return returnVal == 0
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return false
        }
    }

    fun TextView.setColorOfSubstring(substring: String, color: Int) {
        try {
            val spannable = android.text.SpannableString(text)
            val start = text.indexOf(substring)
            spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, color)), start, start + substring.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            text = spannable
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}