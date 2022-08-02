package ru.murzify.bitcoinexplorer.presentation.fragments


import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.murzify.bitcoinexplorer.R
import ru.murzify.bitcoinexplorer.presentation.SearchViewModel
import ru.murzify.bitcoinexplorer.databinding.FragmentTransactionBinding
import ru.murzify.bitcoinexplorer.presentation.Utils.onCopyClickListener
import ru.murzify.bitcoinexplorer.presentation.adapters.InputsAdapter
import ru.murzify.btc.adapters.OutsAdapter

private const val ARG_HASH = "hash"


class TransactionFragment : Fragment() {
    private val myViewModel: SearchViewModel by viewModel()
    private val inputsAdapter = InputsAdapter()
    private val outsAdapter = OutsAdapter()

    private lateinit var hash: String

    private lateinit var binding: FragmentTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hash = arguments!!.getString(ARG_HASH).toString()

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
//                transactionHash.setOnLongClickListener(onCopy)
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
            myViewModel.transactionData.observe(viewLifecycleOwner) { transaction ->

                progressBar.visibility = View.GONE

                // хэш транзакции
//                transactionHash.text = transaction.hash

                // статус транзакции
                when (transaction.status){
                    true -> statusTransaction.setTextColor(Color.GREEN)
                    false -> statusTransaction.setTextColor(Color.RED)
                }
                statusTransaction.text = when (transaction.status) {
                    true -> getString(R.string.available)
                    false -> getString(R.string.unavailable)
                }

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

            progressBar.visibility = View.VISIBLE

            myViewModel.getDataTransaction(hash)

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