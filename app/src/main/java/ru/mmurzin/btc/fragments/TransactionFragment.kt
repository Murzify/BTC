package ru.mmurzin.btc.fragments


import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import ru.mmurzin.btc.MyViewModel
import ru.mmurzin.btc.R
import ru.mmurzin.btc.Utils.onCopyClickListener
import ru.mmurzin.btc.adapters.InputsAdapter
import ru.mmurzin.btc.adapters.OutsAdapter
import ru.mmurzin.btc.databinding.FragmentTransactionBinding
import kotlin.coroutines.CoroutineContext

private const val ARG_HASH = "hash"


class TransactionFragment : Fragment(), CoroutineScope {
    private val myViewModel: MyViewModel by activityViewModels()
    private val inputsAdapter = InputsAdapter()
    private val outsAdapter = OutsAdapter()

    private lateinit var hash: String

    private lateinit var binding: FragmentTransactionBinding

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
        binding = FragmentTransactionBinding.inflate(inflater)
        activity?.let { activity ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            binding.apply {
                //если фрагмент был вызван с параметром
                setDataTransaction()


                // установка адапетра входов
                rvInputs.layoutManager = LinearLayoutManager(activity)
                rvInputs.adapter = inputsAdapter
                // установка адапетра выходов
                rvOuts.layoutManager = LinearLayoutManager(activity)
                rvOuts.adapter = outsAdapter

                // слушатель долгого нажатия для копирования текста
                val onCopy = onCopyClickListener(activity, clipboard)

                // установка слушателя на TextView
                statusTransaction.setOnLongClickListener(onCopy)
                time.setOnLongClickListener(onCopy)
                size.setOnLongClickListener(onCopy)
                numBlock.setOnLongClickListener(onCopy)
                generalInput.setOnLongClickListener(onCopy)
                generalOutput.setOnLongClickListener(onCopy)
                fee.setOnLongClickListener(onCopy)

            }






        }
        return binding.root
    }



    override fun onStart() {
        super.onStart()
        binding.apply {
            myViewModel.transaction.observe(viewLifecycleOwner) {   transaction ->
                //статус транзакции
                when (transaction.status){
                    getString(R.string.available) -> statusTransaction.setTextColor(Color.GREEN)
                    getString(R.string.unavailable) -> statusTransaction.setTextColor(Color.RED)
                }
                statusTransaction.text = transaction.status

                // форматированное время транзакции
                time.text = transaction.f_time

                //размер блока
                size.text = getString(R.string.data_size, transaction.size)

                // номер блока
                numBlock.text = transaction.block_index.toString()

                // общие входы и выходы
                generalInput.text = getString(
                    R.string.price_transaction,
                    transaction.f_input)

                generalOutput.text = getString(
                    R.string.price_transaction,
                    transaction.f_out)

                // комиссия
                binding.fee.text = getString(
                    R.string.price_transaction,
                    transaction.fee)

                //очистка списка входов и выходов перед выводом новых
                inputsAdapter.clear()
                outsAdapter.clear()

                for(input in transaction.inputs){
                    inputsAdapter.addInput(input)
                }
                for(out in transaction.out){
                    outsAdapter.addOut(out)
                }
                scroll.visibility = View.VISIBLE
            }
        }
    }

    private fun setDataTransaction(){
        binding.apply {
            launch(Dispatchers.Main) {
                progressBar.visibility = View.VISIBLE
                withContext(Dispatchers.IO){
                    myViewModel.getDataTransaction(hash)
                }
                progressBar.visibility = View.GONE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            TransactionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_HASH, param1)
                }
            }
    }
}