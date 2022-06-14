package ru.mmurzin.btc

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Response
import retrofit2.awaitResponse
import ru.mmurzin.btc.api.Apifactory
import ru.mmurzin.btc.api.blockchainInfo.responce.Transaction
import ru.mmurzin.btc.api.blockchair.responce.Repo
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

class MyViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private lateinit var job: Job
    // Inherit CoroutineScope должен инициализировать переменную coroutineContext
    // Это стандартный метод записи, + на самом деле метод plus, указывающий задание впереди, используемый для управления сопрограммами, за которым следуют диспетчеры, определяющие поток для запуска
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val transaction = MutableLiveData<Transaction>()
    val info = MutableLiveData<Repo>()

    fun updateInfo(data: Repo){
        info.value = data
    }

    private fun updateTransaction(data: Transaction){
        transaction.postValue(data)
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
                data.gen_input += input.prev_out.value.toDouble()
            }
            for (out in data.out){
                data.gen_out += out.value.toDouble()
            }
            data.gen_out = data.gen_out / 100000000
            data.gen_input = data.gen_input / 100000000

            // комиссия
            data.fee = data.fee / 100000000
            updateTransaction(data)
        }


        return result
    }
}