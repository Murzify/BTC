package ru.mmurzin.btc.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.*
import retrofit2.awaitResponse
import ru.mmurzin.btc.MyViewModel
import ru.mmurzin.btc.R
import ru.mmurzin.btc.api.Apifactory
import ru.mmurzin.btc.databinding.FragmentInfoBinding
import kotlin.coroutines.CoroutineContext


class InfoFragment : Fragment(), CoroutineScope {

    private lateinit var binding: FragmentInfoBinding

    private lateinit var job: Job
    // Inherit CoroutineScope должен инициализировать переменную coroutineContext
    // Это стандартный метод записи, + на самом деле метод plus, указывающий задание впереди, используемый для управления сопрограммами, за которым следуют диспетчеры, определяющие поток для запуска
    override val coroutineContext: CoroutineContext
    get() = job + Dispatchers.Main

    private val myViewModel: MyViewModel by activityViewModels()

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
            launch(Dispatchers.Main) {
                blocks.text = "..."
                addresses.text = "..."
                transactions.text = "..."
                progressBar.visibility = View.VISIBLE
                val result = withContext(Dispatchers.IO) {
                    Apifactory.blockchair.getBlockchainStats("bitcoin").awaitResponse()
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
                transactions.text = getString(R.string.transactions, it.data.transactions)
                addresses.text = getString(R.string.addresses, it.data.hodling_addresses)
                fee.text =
                    getString(R.string.fee_format, it.data.suggested_transaction_fee_per_byte_sat)
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