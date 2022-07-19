package ru.murzify.btc

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.murzify.btc.databinding.ActivityMainBinding
import ru.murzify.btc.fragments.InfoFragment
import ru.murzify.btc.fragments.SearchFragment


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)

        if (!isOnline(caps)){
            binding.apply {
                bottomNavigationView.visibility = View.GONE
                animationView.visibility = View.VISIBLE
                noConnection.visibility = View.VISIBLE
            }
        }


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                // информация о биткоине, цена, кол-во блоков, кол-во транзакций...
                R.id.info_page -> {
                    supportFragmentManager.commit {
                        replace<InfoFragment>(R.id.fragment_view)
                    }
                    true
                }
                // поиск блоков, транзакций, адресов...
                R.id.search_page -> {
                    supportFragmentManager.commit {
                        replace<SearchFragment>(R.id.fragment_view)
                    }
                    true
                }

                else -> false
            }


        }

        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network : Network) {
                runOnUiThread {
                    binding.apply {
                        bottomNavigationView.visibility = View.VISIBLE
                        animationView.visibility = View.GONE
                        noConnection.visibility = View.GONE
                        bottomNavigationView.selectedItemId = R.id.info_page
                    }
                }

            }

            override fun onLost(network : Network) {
                runOnUiThread {
                    binding.apply {
                        bottomNavigationView.visibility = View.GONE
                        animationView.visibility = View.VISIBLE
                        noConnection.visibility = View.VISIBLE
                    }
                }
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_view)
                supportFragmentManager.commit {
                    remove(fragment!!)
                }
            }

        })

    }

    private fun isOnline(caps: NetworkCapabilities?): Boolean{
        if (caps != null) {
            if (caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                return true
            }
        }
        return false
    }
}