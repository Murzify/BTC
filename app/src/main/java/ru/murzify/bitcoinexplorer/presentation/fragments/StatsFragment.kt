package ru.murzify.bitcoinexplorer.presentation.fragments

import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.murzify.bitcoinexplorer.R
import ru.murzify.bitcoinexplorer.databinding.FragmentStatsBinding
import ru.murzify.bitcoinexplorer.domain.models.Value
import ru.murzify.bitcoinexplorer.presentation.StatsViewModel
import ru.murzify.bitcoinexplorer.presentation.Utils
import ru.murzify.bitcoinexplorer.presentation.adapters.ChartAdapter
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding

    private val vm: StatsViewModel by viewModel()

    private val chartAdapter = ChartAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater)

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

                // на пока не получили ответ от api
                scrollView2.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                vm.setGeneralStats()
                vm.getChartData()

                // TODO: возможно создается много корутин
                vm.loadLoopData()


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
            vm.generalStats.observe(viewLifecycleOwner) {

                scrollView2.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

                priceBtcUsd.text = getString(R.string.btc_price, it.data.market_price_usd)
                blocks.text = getString(R.string.blocks, it.data.blocks)
                transactions.text = getString(R.string.info_transactions, it.data.transactions)
                addresses.text = getString(R.string.addresses, it.data.hodling_addresses)
                fee.text =
                    getString(R.string.fee_format, it.data.suggested_transaction_fee_per_byte_sat)
            }
            vm.chartData.observe(viewLifecycleOwner) {
                when(it.isUp){
                    true -> chart.lineColor = Color.GREEN
                    false -> chart.lineColor = Color.RED
                }
                chartAdapter.setData(it.values)
            }
        }
    }
}