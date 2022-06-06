package ru.mmurzin.btc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Response
import retrofit2.awaitResponse
import ru.mmurzin.btc.api.Apifactory
import ru.mmurzin.btc.api.blockchainInfo.responce.Transaction
import ru.mmurzin.btc.api.blockchair.responce.Repo
import kotlin.coroutines.CoroutineContext

class MyViewModel: ViewModel(), CoroutineScope {

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
            updateTransaction(result.body()!!)
        }
        return result
    }
}