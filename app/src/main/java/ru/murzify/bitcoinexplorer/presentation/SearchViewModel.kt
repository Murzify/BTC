package ru.murzify.bitcoinexplorer.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.murzify.bitcoinexplorer.domain.models.AddressData
import ru.murzify.bitcoinexplorer.domain.models.BlockData
import ru.murzify.bitcoinexplorer.domain.models.DataBlock
import ru.murzify.bitcoinexplorer.domain.models.TransactionData
import ru.murzify.bitcoinexplorer.domain.usecase.GetDataAddressUseCase
import ru.murzify.bitcoinexplorer.domain.usecase.GetDataBlockUseCase
import ru.murzify.bitcoinexplorer.domain.usecase.GetDataTransactionUseCase

class SearchViewModel(
    private val getDataAddressUseCase: GetDataAddressUseCase,
    private val getDataBlockUseCase: GetDataBlockUseCase,
    private val getDataTransactionUseCase: GetDataTransactionUseCase
): ViewModel() {

    private val addressDataMutable by lazy { MutableLiveData<AddressData>() }
    val addressData: LiveData<AddressData> = addressDataMutable

    private val blockDataMutable by lazy { MutableLiveData<DataBlock>() }
    val blockData: LiveData<DataBlock> = blockDataMutable

    private val transactionDataMutable by lazy { MutableLiveData<TransactionData>() }
    val transactionData: LiveData<TransactionData> = transactionDataMutable

    fun getDataAddress(address: String, offset: Int){
        viewModelScope.launch(Dispatchers.IO) {

            getDataAddressUseCase.execute(address, offset).fold(
                {},
                {
                    if (offset == 0) {
                        addressDataMutable.postValue(it)
                    } else {
                        addressDataMutable.value!!.txs.addAll(it.txs)
                    }
                }

            )

        }
    }

    fun getDataBlock(blockId: String){
        viewModelScope.launch(Dispatchers.IO) {

            //кривое api blockchair, ставлю лайк
            val hash = when (blockId) {
                "0" -> "000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f"
                else -> blockId
            }

            getDataBlockUseCase.execute(hash).fold(
                {},
                {
                    if (it.data.isNotEmpty()){
                        blockDataMutable.postValue(it.data[hash])
                    }
                }

            )

        }
    }

    fun getDataTransaction(hash: String){
        viewModelScope.launch(Dispatchers.IO) {

            getDataTransactionUseCase.execute(hash).fold(
                {},
                {
                    transactionDataMutable.postValue(it)
                }

            )

        }
    }

}