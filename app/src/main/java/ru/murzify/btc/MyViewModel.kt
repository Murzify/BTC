package ru.murzify.btc

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import ru.murzify.btc.Utils.timeFormat
import ru.murzify.btc.api.blockchainInfo.responce.Address
import ru.murzify.btc.api.blockchainInfo.responce.Chart
import ru.murzify.btc.api.blockchainInfo.responce.Transaction
import ru.murzify.btc.api.blockchair.responce.DataBlock
import ru.murzify.btc.api.blockchair.responce.Info
import ru.murzify.btc.domain.usecase.*
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

    private val getBitcoinGeneralStatsUseCase = GetBitcoinGeneralStatsUseCase()
    private val getBitcoinChartDataUseCase = GetBitcoinChartDataUseCase()
    private val getDataTransactionUseCase = GetDataTransactionUseCase()
    private val getDataAddressUseCase = GetDataAddressUseCase()
    private val getDataBlockUseCase = GetDataBlockUseCase()

    val transaction = MutableLiveData<Transaction>()
    val info = MutableLiveData<Info>()
    val chartData = MutableLiveData<Chart>()
    val address = MutableLiveData<Address>()
    val block = MutableLiveData<DataBlock>()

    suspend fun getDataBlock(blockId: String){
        val result = getDataBlockUseCase.execute(blockId)

        result?.let {
            if (result.data.isNotEmpty()){
                val data = result.data[getDataBlockUseCase.hash]!!
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

    }

    suspend fun getDataAddress(addr: String, offset: Int): Address? {
        val result = getDataAddressUseCase.execute(addr, offset)
        result?.let {
            // выведутся перывые 100 транзакций
            // если offset > 0 то нужно вывести последующие транзакции,
            // сохранять во ViewModel их не нужно
            if (offset == 0){
                result.f_final_balance = result.final_balance / 100000000.0
                result.f_total_received = result.total_received / 100000000.0
                result.f_total_sent = result.total_sent / 100000000.0
                address.postValue(result)
            }
        }
        return result
    }

    suspend fun getDataInfo() {
        val result = getBitcoinGeneralStatsUseCase.execute()
        if (result != null){
            info.postValue(result)
        }
    }

    // постоянное обновление информации
    suspend fun loadLoopData(){
        while (true){
            val result = getBitcoinGeneralStatsUseCase.execute()

            result?.let {
                info.postValue(result)
            }

            delay(30000)
        }
    }

    suspend fun getChart(){
        val result = getBitcoinChartDataUseCase.execute()
        result?.let {
            result.values.also { values ->
                // вычисление изменения курса биткоина в процентах
                val start = values[0].y
                val end = values[values.size - 1].y
                var isUp = false
                if (start < end){
                    isUp = true
                }
                val percent = abs((start-end)/((start+end)/2)) * 100
                result.isUp = isUp
                result.percent = percent
            }
            chartData.postValue(result)
        }
    }

    suspend fun getDataTransaction(hash: String){
        val result = getDataTransactionUseCase.execute(hash)

        result?.let {
            //статус транзакции
            when (result.block_index) {
                //подтверждена
                null -> result.status = getApplication<Application>().resources.getString(R.string.unavailable)
                //не подтверждена
                else -> result.status = getApplication<Application>().resources.getString(R.string.available)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                result.f_time = timeFormat(result.time)
            }

            // общие входы и выходы
            for (input in result.inputs){
                result.f_input += input.prev_out.value.toDouble()
            }
            for (out in result.out){
                result.f_out += out.value.toDouble()
            }
            result.f_out = result.f_out / 100000000
            result.f_input = result.f_input / 100000000

            // комиссия
            result.fee = result.fee / 100000000

            transaction.postValue(result)
        }

    }

}