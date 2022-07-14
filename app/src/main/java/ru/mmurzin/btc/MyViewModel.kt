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
import ru.mmurzin.btc.api.blockchair.responce.Block
import ru.mmurzin.btc.api.blockchair.responce.DataBlock
import ru.mmurzin.btc.api.blockchair.responce.Info
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs

class MyViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val transaction = MutableLiveData<Transaction>()
    val info = MutableLiveData<Info>()
    val chartData = MutableLiveData<Chart>()
    val address = MutableLiveData<Address>()
    val block = MutableLiveData<DataBlock>()

    private fun updateTransaction(data: Transaction){
        transaction.postValue(data)
    }

    suspend fun getDataBlock(blockId: String): Response<Block>{
        // так пришлось сделать из-за кривого API Blockchair
        var hash = blockId
        if (blockId == "0"){
            hash = "000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f"
        }
        val result = Apifactory.blockchair.getBlockInfo(hash).awaitResponse()
        if (result.isSuccessful){
            if (result.body()!!.data.isNotEmpty()){
                val data = result.body()!!.data[hash]!!
                data.block.also {
                    it.f_input_total = data.block.input_total / 100000000.0
                    val parseTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.block.time)
                    it.f_time = Timestamp(parseTime!!.time)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .format(
                            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                        )
                    it.f_fee_total = data.block.fee_total / 100000000.0
                    it.f_generation = data.block.generation / 100000000.0
                }
                block.postValue(data)
            }

        }
        return result
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
                data.f_final_balance = data.final_balance / 100000000.0
                data.f_total_received = data.total_received / 100000000.0
                data.f_total_sent = data.total_sent / 100000000.0
                address.postValue(data)
            }
        }
        return result
    }

    suspend fun getDataInfo() {
        val result = Apifactory.blockchair.getBlockchainStats("bitcoin").awaitResponse()
        if (result.isSuccessful){
            result.body()?.also {
                info.postValue(it)
            }
        }
    }

    // постоянное обновление информации
    suspend fun loadLoopData(){
        while (true){
            val result = withContext(Dispatchers.IO) {
                Apifactory.blockchair.getBlockchainStats("bitcoin").awaitResponse()
            }
            if (result.isSuccessful) {
                result.body()?.also {
                    info.postValue(it)
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
            data.values.also { values ->
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
                data.f_time = timeFormat(data.time)
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

    private fun timeFormat(time: Long): String{
        return Timestamp(time * 1000)
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            )
    }
}