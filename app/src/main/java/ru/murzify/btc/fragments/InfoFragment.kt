package ru.murzify.btc.fragments

import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.murzify.btc.MyViewModel
import ru.murzify.btc.R
import ru.murzify.btc.Utils
import ru.murzify.btc.adapters.ChartAdapter
import ru.murzify.btc.api.blockchainInfo.responce.Value
import ru.murzify.btc.databinding.FragmentInfoBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater)

        activity?.let { activity ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
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

                    myViewModel.getDataInfo()
                    myViewModel.getChart()

                    progressBar.visibility = View.GONE
                    scrollView2.visibility = View.VISIBLE

                    myViewModel.loadLoopData()
                }

                // слушатель долгого нажатия для копирования текста
                val onCopy = Utils.onCopyClickListener(activity, clipboard)

                priceBtcUsd.setOnLongClickListener(onCopy)
                fee.setOnLongClickListener(onCopy)
                blocks.setOnLongClickListener(onCopy)
                transactions.setOnLongClickListener(onCopy)
                addresses.setOnLongClickListener(onCopy)
            }
        }

        return binding.root
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