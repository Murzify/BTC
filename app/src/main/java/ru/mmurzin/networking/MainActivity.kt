package ru.mmurzin.networking

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.*
import retrofit2.awaitResponse
import ru.mmurzin.networking.api.Apifactory.blockchair
import ru.mmurzin.networking.databinding.ActivityMainBinding
import ru.mmurzin.networking.fragments.InfoFragment
import ru.mmurzin.networking.fragments.TransactionFragment
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_view,
                InfoFragment.newInstance()
            )
            .commit()


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                //информация о биткоине, цена, кол-во блоков, кол-во транзакций...
                R.id.info_page -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.fragment_view,
                            InfoFragment.newInstance()
                        )
                        .commit()
                    true
                }
                //получить информацию о транзакции по хэшу
                R.id.transaction_page -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.fragment_view,
                            TransactionFragment.newInstance(null)
                        )
                        .commit()
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



    }



}