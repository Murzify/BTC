package ru.mmurzin.networking

import android.content.Context
import android.net.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*
import retrofit2.awaitResponse
import ru.mmurzin.networking.Apifactory.blockchair
import ru.mmurzin.networking.databinding.ActivityMainBinding
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {

    private val myViewModel: MyViewModel by lazy { ViewModelProvider(this).get(MyViewModel::class.java) }

    private lateinit var binding: ActivityMainBinding
    // задание используется для управления сопрограммой, сопрограмма запускается после запуска {}, возвращаемое задание - это объект задания
    private lateinit var job: Job
    // Inherit CoroutineScope должен инициализировать переменную coroutineContext
    // Это стандартный метод записи, + на самом деле метод plus, указывающий задание впереди, используемый для управления сопрограммами, за которым следуют диспетчеры, определяющие поток для запуска
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Инициируем работу в onCreate
        job = Job()

        binding.apply {
            launch(Dispatchers.Main) {
                blocks.text = "..."
                addresses.text = "..."
                transactions.text = "..."
                progressBar.visibility = View.VISIBLE
                val result = withContext(Dispatchers.IO) {
                    blockchair.getBlockchainStats("bitcoin").awaitResponse()
                }
                Log.d("mark", result.toString())
                progressBar.visibility = View.GONE

                if (result.isSuccessful) {
                    result.body()?.also {
                        myViewModel.updateData(it)
                    }
                }
                loadLoopData()
            }

        }

    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            myViewModel.repo.observe(this@MainActivity, Observer{
                priceBtcUsd.text = getString(R.string.price, it.context.market_price_usd)
                blocks.text = getString(R.string.blocks, it.data.blocks)
                transactions.text = getString(R.string.transactions, it.data.transactions)
                addresses.text = getString(R.string.addresses, it.data.hodling_addresses)
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Когда действие закончено, нам больше не нужно запрашивать сеть и завершать текущую сопрограмму
        job.cancel()
    }

    private suspend fun loadLoopData(){
        while (true){
            val result = withContext(Dispatchers.IO) {
                blockchair.getBlockchainStats("bitcoin").awaitResponse()
            }
            if (result.isSuccessful) {
                result.body()?.also {
                    myViewModel.updateData(it)
                }
            }

            delay(30000)
        }
    }

    fun isConnectedToInternet(): Boolean {
        val connectivity =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (i in info.indices) if (info[i].state == NetworkInfo.State.CONNECTED) {
            return true
        }
        return false
    }


}