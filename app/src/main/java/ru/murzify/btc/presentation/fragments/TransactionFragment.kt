package ru.murzify.btc.presentation.fragments


import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.murzify.btc.MyViewModel
import ru.murzify.btc.R
import ru.murzify.btc.Utils.onCopyClickListener
import ru.murzify.btc.adapters.InputsAdapter
import ru.murzify.btc.adapters.OutsAdapter
import ru.murzify.btc.databinding.FragmentTransactionBinding
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

                val llm = object : LinearLayoutManager(activity) {
                    override fun onLayoutChildren(
                        recycler: RecyclerView.Recycler?,
                        state: RecyclerView.State?
                    ) {
                        try {
                            super.onLayoutChildren(recycler, state)
                        } catch (e: IndexOutOfBoundsException) {
                            Toast.makeText(activity, "rv error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                // установка адапетра входов
                rvInputs.layoutManager = llm
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

                myViewModel.getDataTransaction(hash)

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