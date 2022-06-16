package ru.mmurzin.btc

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.mmurzin.btc.databinding.ActivityMainBinding
import ru.mmurzin.btc.fragments.InfoFragment
import ru.mmurzin.btc.fragments.TransactionFragment


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)

        if (isOnline(caps)){
            supportFragmentManager.commit {
                replace<InfoFragment>(R.id.fragment_view)
                setReorderingAllowed(true)
            }
        } else {
            binding.apply {
                bottomNavigationView.visibility = View.GONE
                animationView.visibility = View.VISIBLE
                noConnection.visibility = View.VISIBLE
            }
        }


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                //информация о биткоине, цена, кол-во блоков, кол-во транзакций...
                R.id.info_page -> {
                    supportFragmentManager.commit {
                        replace<InfoFragment>(R.id.fragment_view)
                        setReorderingAllowed(true)
                    }
                    true
                }
                //получить информацию о транзакции по хэшу
                R.id.transaction_page -> {
                    supportFragmentManager.commit {
                        replace<TransactionFragment>(R.id.fragment_view)
                        setReorderingAllowed(true)
                    }
                    true
                }
                //получить информцию об адресе
                R.id.wallet_page -> {
                    true
                }
                //настройки, контакты, история...
                R.id.me_page -> {
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
                supportFragmentManager.commit {
                    replace<InfoFragment>(R.id.fragment_view)
                    setReorderingAllowed(true)
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
            if (caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }



}