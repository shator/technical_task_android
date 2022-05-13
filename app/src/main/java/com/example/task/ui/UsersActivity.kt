package com.example.task.ui

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task.R
import com.example.task.databinding.ActivityUsersBinding
import com.example.task.ui.userslist.UsersListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersActivity : AppCompatActivity(R.layout.activity_users) {

    private lateinit var binding: ActivityUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (!isNetworkConnected()) {
            Toast.makeText(this, "internet not available", Toast.LENGTH_LONG).show()
            return
        }
        if (savedInstanceState == null) {
            showUsersListFragment()
        }
    }

    private fun showUsersListFragment() {
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container_view,
            UsersListFragment.newInstance()
        ).commit()
    }

    private fun isNetworkConnected(): Boolean {
        val cm: ConnectivityManager = applicationContext?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = cm.activeNetwork ?: return false
            val networkCapabilities = cm.getNetworkCapabilities(activeNetwork) ?: return false
            (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        } else {
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}