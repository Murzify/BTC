package ru.mmurzin.btc.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.robinhood.spark.SparkAdapter
import kotlinx.coroutines.*
import retrofit2.awaitResponse
import ru.mmurzin.btc.MyViewModel
import ru.mmurzin.btc.R
import ru.mmurzin.btc.api.Apifactory
import ru.mmurzin.btc.api.blockchainInfo.responce.Value
import ru.mmurzin.btc.databinding.FragmentInfoBinding
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.typeOf


class InfoFragment : Fragment(), CoroutineScope {

    private lateinit var binding: FragmentInfoBinding

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val myViewModel: MyViewModel by activityViewModels()

    private val chartAdapter = ChartAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        job = Job()

        binding.apply {
            chart.adapter = chartAdapter
            chart.isScrubEnabled = true
            chart.setScrubListener { value ->
                if (value is Value){
                    val date = Timestamp(value.x * 1000)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .format(
                            DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        )
                    sparkText.text = getString(
                        R.string.text_spark,
                        value.y,
                        date
                    )
                }
            }
            launch(Dispatchers.Main) {
                blocks.text = "..."
                addresses.text = "..."
                transactions.text = "..."
                progressBar.visibility = View.VISIBLE
                val result = withContext(Dispatchers.IO) {
                    Apifactory.blockchair.getBlockchainStats("bitcoin").awaitResponse()
                }
                withContext(Dispatchers.IO){
                    myViewModel.getChart()
                }
                Log.d("mark", result.toString())
                progressBar.visibility = View.GONE

                if (result.isSuccessful) {
                    result.body()?.also {
                        myViewModel.updateInfo(it)
                    }
                }
                loadLoopData()
            }


        }

    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            myViewModel.info.observe(viewLifecycleOwner) {
                priceBtcUsd.text = getString(R.string.btc_price, it.context.market_price_usd)
                blocks.text = getString(R.string.blocks, it.data.blocks)
                transactions.text = getString(R.string.info_transactions, it.data.transactions)
                addresses.text = getString(R.string.addresses, it.data.hodling_addresses)
                fee.text =
                    getString(R.string.fee_format, it.data.suggested_transaction_fee_per_byte_sat)
            }
            myViewModel.chartData.observe(viewLifecycleOwner) {
                when(it.isUp){
                    true -> chart.lineColor = Color.GREEN
                    false -> chart.lineColor = Color.RED
                }
                chartAdapter.setData(it.values)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        @JvmStatic
        fun newInstance() = InfoFragment()
    }

    private suspend fun loadLoopData(){
        while (true){
            val result = withContext(Dispatchers.IO) {
                Apifactory.blockchair.getBlockchainStats("bitcoin").awaitResponse()
            }
            if (result.isSuccessful) {
                result.body()?.also {
                    myViewModel.updateInfo(it)
                }
            }

            delay(30000)
        }
    }
}

class ChartAdapter: SparkAdapter(){
    private var data:List<Value> = listOf()

    fun setData(list: List<Value>){
        data = list
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(index: Int): Any {
        return data[index]
    }

    override fun getY(index: Int): Float {
        return data[index].y.toFloat()
    }

    override fun getX(index: Int): Float {
        return data[index].x.toFloat()
    }

}