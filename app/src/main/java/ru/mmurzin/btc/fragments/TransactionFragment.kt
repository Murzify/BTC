package ru.mmurzin.btc.fragments

import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Build

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager


import kotlinx.coroutines.*
import ru.mmurzin.btc.InputsAdapter
import ru.mmurzin.btc.MyViewModel
import ru.mmurzin.btc.OutsAdapter
import ru.mmurzin.btc.R
import ru.mmurzin.btc.databinding.FragmentTransactionBinding
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

private const val ARG_HASH = "hash"


class TransactionFragment : Fragment(), CoroutineScope {
    private val myViewModel: MyViewModel by activityViewModels()

    private val inputsAdapter = InputsAdapter()
    private val outsAdapter = OutsAdapter()

    private var hash: String? = null

    private lateinit var binding: FragmentTransactionBinding

    private lateinit var job: Job
    // Inherit CoroutineScope должен инициализировать переменную coroutineContext
    // Это стандартный метод записи, + на самом деле метод plus, указывающий задание впереди, используемый для управления сопрограммами, за которым следуют диспетчеры, определяющие поток для запуска
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hash = it.getString(ARG_HASH)
        }
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { activity ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            binding.apply {
                pasteBtn.setOnClickListener {
                    clipboard.primaryClip?.let {
                        val item = it.getItemAt(0)
                        hashInput.setText(item.text)
                        setDataTransaction()
                    }
                }
                // установка адапетра входов
                rvInputs.layoutManager = LinearLayoutManager(activity)
                rvInputs.adapter = inputsAdapter
                // установка адапетра выходов
                rvOuts.layoutManager = LinearLayoutManager(activity)
                rvOuts.adapter = outsAdapter
            }

        }

        // отслеживание нажатия галочки на клавиатуре
        binding.hashInput.setOnEditorActionListener { textView, i, keyEvent ->

            if (i == EditorInfo.IME_ACTION_DONE){
                setDataTransaction()
            }
            return@setOnEditorActionListener false
        }
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            myViewModel.transaction.observe(viewLifecycleOwner) {   transaction ->
                // проверка статуса транзакции
                when (transaction.block_index) {
                    null -> {
                        statusTransaction.text = getString(R.string.unavailable)
                        statusTransaction.setTextColor(Color.RED)
                    }
                    else -> {
                        statusTransaction.text = getString(R.string.available)
                        statusTransaction.setTextColor(Color.GREEN)
                    }
                }
                // форматирование времени транзакции
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    time.text =
                        Timestamp(transaction.time * 1000)
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .format(
                                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                            )
                }
                size.text = getString(R.string.data_size, transaction.size)
                numBlock.text = transaction.block_index.toString()

                //общие входы и выходы
                var genInput: Long = 0
                var genOut: Long = 0

                for (input in transaction.inputs){
                    genInput += input.prev_out.value
                }
                for (out in transaction.out){
                    genOut += out.value
                }

                generalInput.text = getString(
                    R.string.price_transaction,
                    genInput.toDouble() / 100000000)

                generalOut.text = getString(
                    R.string.price_transaction,
                    genOut.toDouble() / 100000000)

                val fee: Long = genInput - genOut
                binding.fee.text = getString(
                    R.string.price_transaction,
                    fee.toDouble() / 100000000)
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
                hashInputLayout.error = null
                progressBar.visibility = View.VISIBLE
                val result = withContext(Dispatchers.IO){
                    myViewModel.getDataTransaction(hashInput.text.toString())
                }
                progressBar.visibility = View.GONE
                if (!result.isSuccessful){
                    hashInputLayout.error = getString(R.string.hash_error)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String?) =
            TransactionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_HASH, param1)
                }
            }
    }
}