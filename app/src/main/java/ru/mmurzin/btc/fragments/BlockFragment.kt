package ru.mmurzin.btc.fragments

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import ru.mmurzin.btc.MyViewModel
import ru.mmurzin.btc.R
import ru.mmurzin.btc.adapters.BlockTransactionsAdapter
import ru.mmurzin.btc.databinding.FragmentBlockBinding
import kotlin.coroutines.CoroutineContext

class BlockFragment : Fragment(), CoroutineScope{

    private lateinit var binding: FragmentBlockBinding
    private val myViewModel: MyViewModel by activityViewModels()
    private val transactionsAdapter = BlockTransactionsAdapter{ hash ->
        onTransactionClick(hash)
    }

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlockBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { activity ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            binding.apply {
                pasteBtn.setOnClickListener {
                    clipboard.primaryClip?.let {
                        val item = it.getItemAt(0)
                        hashInput.setText(item.text)
                        setDataBlock()
                    }
                }
                rvTransactions.layoutManager = LinearLayoutManager(activity)
                rvTransactions.adapter = transactionsAdapter
            }

        }

        // отслеживание нажатия галочки на клавиатуре
        binding.hashInput.setOnEditorActionListener { _, i, _ ->

            if (i == EditorInfo.IME_ACTION_DONE){
                setDataBlock()
            }
            return@setOnEditorActionListener false
        }
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            myViewModel.block.observe(viewLifecycleOwner) {
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
                hashInputLayout.error = null
                progressBar.visibility = View.VISIBLE
                val result = withContext(Dispatchers.IO){
                    myViewModel.getDataBlock(hashInput.text.toString())
                }
                progressBar.visibility = View.GONE
                if (!result.isSuccessful){
                    if (result.code() == 429){
                        hashInputLayout.error = getString(R.string.too_many_requests)
                    } else {
                        hashInputLayout.error = getString(R.string.hash_error)
                        Log.d("networkbtc", result.code().toString()+" "+result.message())
                    }
                }
            }
        }
    }

    private fun onTransactionClick(hash: String){
        parentFragmentManager
            .beginTransaction()
            .replace((view!!.parent as ViewGroup).id, TransactionFragment.newInstance(hash))
            .commit()
    }


}