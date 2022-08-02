package ru.murzify.bitcoinexplorer.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.murzify.bitcoinexplorer.domain.models.ChartData
import ru.murzify.bitcoinexplorer.domain.models.InfoData
import ru.murzify.bitcoinexplorer.domain.usecase.GetChartDataUseCase
import ru.murzify.bitcoinexplorer.domain.usecase.GetGeneralStatsUseCase

class StatsViewModel(
    private val getGeneralStatsUseCase: GetGeneralStatsUseCase,
    private val getChartDataUseCase: GetChartDataUseCase
): ViewModel() {

    private val generalStatsMutable: MutableLiveData<InfoData> by lazy { MutableLiveData<InfoData>() }
    val generalStats: LiveData<InfoData> = generalStatsMutable

    private val chartDataMutable: MutableLiveData<ChartData> by lazy { MutableLiveData<ChartData>() }
    val chartData: LiveData<ChartData> = chartDataMutable

    fun setGeneralStats() {
        viewModelScope.launch(Dispatchers.IO) {
            getGeneralStatsUseCase.execute().also {
                it.fold(
                    {},
                    { data ->
                        generalStatsMutable.postValue(data)
                    }
                )
            }
        }
    }

    fun getChartData() {
        viewModelScope.launch(Dispatchers.IO) {
            getChartDataUseCase.execute().also {
                it.fold(
                    {},
                    { data ->
                        chartDataMutable.postValue(data)
                    }
                )
            }
        }
    }

    // постоянное обновление информации
    fun loadLoopData(){
        viewModelScope.launch(Dispatchers.IO) {
            while (true){
                getChartData()
                delay(30000)
            }
        }
    }
}