package ru.mmurzin.btc

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.awaitResponse
import ru.mmurzin.btc.api.Apifactory
import ru.mmurzin.btc.api.blockchainInfo.responce.Address
import ru.mmurzin.btc.api.blockchainInfo.responce.Chart
import ru.mmurzin.btc.api.blockchainInfo.responce.Transaction
import ru.mmurzin.btc.api.blockchainInfo.responce.Tx
import ru.mmurzin.btc.api.blockchair.responce.Repo
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs

class MyViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private lateinit var job: Job
    // Inherit CoroutineScope должен инициализировать переменную coroutineContext
    // Это стандартный метод записи, + на самом деле метод plus, указывающий задание впереди, используемый для управления сопрограммами, за которым следуют диспетчеры, определяющие поток для запуска
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val transaction = MutableLiveData<Transaction>()
    val info = MutableLiveData<Repo>()
    val chartData = MutableLiveData<Chart>()
    val address = MutableLiveData<Address>()
    val txs = MutableLiveData<List<Tx>>()

    fun updateInfo(data: Repo){
        info.postValue(data)
    }

    private fun updateTransaction(data: Transaction){
        transaction.postValue(data)
    }

    suspend fun getDataAddress(addr: String, offset: Int): Response<Address> {
        val result = Apifactory.blockchainInfo.getAddressInfo(addr, mapOf(
            "offset" to offset.toString()
        )).awaitResponse()
        if (result.isSuccessful) {
            // выведутся перывые 100 транзакций
            // если offset > 0 то нужно вывести последующие транзакции,
            // сохранять во ViewModel их не нужно
            if (offset == 0){
                val data = result.body()!!
                data.f_final_balance = data.final_balance.toDouble() / 100000000
                data.f_total_received = data.total_received.toDouble() / 100000000
                data.f_total_sent = data.total_sent.toDouble() / 100000000
                address.postValue(data)
            }
        }
        return result
    }

    suspend fun getDataInfo() {
        val result = Apifactory.blockchair.getBlockchainStats("bitcoin").awaitResponse()
        if (result.isSuccessful){
            result.body()?.also {
                updateInfo(it)
            }
        }
    }

    suspend fun loadLoopData(){
        while (true){
            val result = withContext(Dispatchers.IO) {
                Apifactory.blockchair.getBlockchainStats("bitcoin").awaitResponse()
            }
            if (result.isSuccessful) {
                result.body()?.also {
                    updateInfo(it)
                }
            }

            delay(30000)
        }
    }

    suspend fun getChart(){
        val result = Apifactory.apiBlockchainInfo.getChart(
            mapOf(
                "timespan" to "1months",
                "rollingAverage" to "1days",
                "format" to "json"
            )
        ).awaitResponse()
        if (result.isSuccessful){
            val data = result.body()!!
            result.body()!!.values.also { values ->
                // вычисление изменения курса биткоина в процентах
                val start = values[0].y
                val end = values[values.size - 1].y
                var isUp = false
                if (start < end){
                    isUp = true
                }
                val percent = abs((start-end)/((start+end)/2)) * 100
                data.isUp = isUp
                data.percent = percent
            }
            chartData.postValue(data)
        }

    }

    suspend fun getDataTransaction(hash: String): Response<Transaction> {
        val result = Apifactory.blockchainInfo.getTransactionInfo(hash).awaitResponse()

        if(result.isSuccessful){
            val data = result.body()!!
            //статус транзакции
            when (data.block_index) {
                //подтверждена
                null -> data.status = getApplication<Application>().resources.getString(R.string.unavailable)
                //не подтверждена
                else -> data.status = getApplication<Application>().resources.getString(R.string.available)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                data.f_time =
                    Timestamp(data.time * 1000)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .format(
                            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                        )
            }

            // общие входы и выходы
            for (input in data.inputs){
                data.f_input += input.prev_out.value.toDouble()
            }
            for (out in data.out){
                data.f_out += out.value.toDouble()
            }
            data.f_out = data.f_out / 100000000
            data.f_input = data.f_input / 100000000

            // комиссия
            data.fee = data.fee / 100000000
            updateTransaction(data)
        }


        return result
    }
}