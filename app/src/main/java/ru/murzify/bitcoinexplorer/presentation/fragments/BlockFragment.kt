package ru.murzify.bitcoinexplorer.presentation.fragments

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.murzify.bitcoinexplorer.R
import ru.murzify.bitcoinexplorer.presentation.SearchViewModel
import ru.murzify.bitcoinexplorer.databinding.FragmentBlockBinding
import ru.murzify.bitcoinexplorer.presentation.Utils.onCopyClickListener
import ru.murzify.btc.adapters.BlockTransactionsAdapter
import kotlin.coroutines.CoroutineContext

private const val ARG_HASH = "hash"

class BlockFragment : Fragment(), CoroutineScope{

    private lateinit var binding: FragmentBlockBinding
    private val myViewModel: SearchViewModel by viewModel()
    private val transactionsAdapter = BlockTransactionsAdapter{ hash ->
        onTransactionClick(hash)
    }

    private lateinit var hash: String

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hash = arguments!!.getString(ARG_HASH).toString()

        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlockBinding.inflate(inflater)

        activity?.let { activity ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            binding.apply {
                setDataBlock()

                rvTransactions.layoutManager = LinearLayoutManager(activity)
                rvTransactions.adapter = transactionsAdapter

                // ?????????????????? ?????????????? ?????????????? ?????? ?????????????????????? ????????????
                val onCopy = onCopyClickListener(activity, clipboard)

                // ?????????????????? ?????????????????? ???? TextView
                blockHash.setOnLongClickListener(onCopy)
                transactionVolume.setOnLongClickListener(onCopy)
                time.setOnLongClickListener(onCopy)
                height.setOnLongClickListener(onCopy)
                totalTransactions.setOnLongClickListener(onCopy)
                weight.setOnLongClickListener(onCopy)
                size.setOnLongClickListener(onCopy)
                fee.setOnLongClickListener(onCopy)
                reward.setOnLongClickListener(onCopy)
                miner.setOnLongClickListener(onCopy)

            }

        }
        return binding.root
    }



    override fun onStart() {
        super.onStart()
        binding.apply {
            myViewModel.blockData.observe(viewLifecycleOwner) {
                blockHash.text = it.block.hash
                transactionVolume.text = getString(
                    R.string.price_transaction,
                    it.block.f_input_total
                )
                time.text = it.block.f_time
                height.text = it.block.id.toString()
                totalTransactions.text = it.block.transaction_count.toString()
                weight.text = getString(
                    R.string.weight_format,
                    it.block.weight
                )
                size.text = getString(
                    R.string.byte_format,
                    it.block.size
                )
                fee.text = getString(
                    R.string.price_transaction,
                    it.block.f_fee_total
                )
                reward.text = getString(
                    R.string.price_transaction,
                    it.block.f_generation
                )
                miner.text = it.block.guessed_miner

                transactionsAdapter.clear()
                for (transaction in it.transactions){
                    transactionsAdapter.addTransaction(transaction)
                }
                scroll.visibility = View.VISIBLE
            }
        }

    }

    private fun setDataBlock(){
        binding.apply {
            launch(Dispatchers.Main) {
                progressBar.visibility = View.VISIBLE

                myViewModel.getDataBlock(hash)

                progressBar.visibility = View.GONE
            }
        }
    }

    private fun onTransactionClick(hash: String){
        parentFragmentManager
            .beginTransaction()
            .replace((view!!.parent as ViewGroup).id, TransactionFragment.newInstance(hash))
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String?) =
            BlockFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_HASH, param1)
                }
            }
    }

}