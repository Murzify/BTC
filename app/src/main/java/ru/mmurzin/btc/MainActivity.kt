package ru.mmurzin.btc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.mmurzin.btc.databinding.ActivityMainBinding
import ru.mmurzin.btc.fragments.InfoFragment
import ru.mmurzin.btc.fragments.TransactionFragment


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