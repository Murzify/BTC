package ru.mmurzin.btc.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.*
import ru.mmurzin.btc.MyViewModel
import ru.mmurzin.btc.R
import ru.mmurzin.btc.adapters.ChartAdapter
import ru.mmurzin.btc.api.blockchainInfo.responce.Value
import ru.mmurzin.btc.databinding.FragmentInfoBinding
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext


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
                scrollView2.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                withContext(Dispatchers.IO) {
                    myViewModel.getDataInfo()
                }
                withContext(Dispatchers.IO){
                    myViewModel.getChart()
                }
                progressBar.visibility = View.GONE
                scrollView2.visibility = View.VISIBLE

                myViewModel.loadLoopData()
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

}