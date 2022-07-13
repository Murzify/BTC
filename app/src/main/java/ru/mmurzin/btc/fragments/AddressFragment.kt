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
import ru.mmurzin.btc.Utils.onCopyClickListener
import ru.mmurzin.btc.adapters.TransactionsAdapter
import ru.mmurzin.btc.databinding.FragmentAddressBinding
import kotlin.coroutines.CoroutineContext


private const val addressParam = "address"

class AddressFragment : Fragment(), CoroutineScope {
    private val myViewModel: MyViewModel by activityViewModels()
    private var address: String? = null
    private lateinit var binding: FragmentAddressBinding
    private val transactionsAdapter = TransactionsAdapter{ hash ->
        onTransactionClick(hash)
    }

    private var n_tx = 0
    private var offset = 0
    private var prevAddress = ""

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            address = it.getString(addressParam)
        }
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)
        activity?.let { activity ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            binding.apply {
                pasteBtn.setOnClickListener {
                    clipboard.primaryClip?.let {
                        val item = it.getItemAt(0)
                        walletInput.setText(item.text)
                        setDataAddress()
                    }
                }
                // установка адапетра транзакций
                rvTransactions.layoutManager = LinearLayoutManager(activity)
                rvTransactions.adapter = transactionsAdapter

                scroll.setOnScrollChangeListener{ v, _, scrollY, _, _ ->
                    if (scrollY == (scroll.getChildAt(0).measuredHeight - v.measuredHeight)) {
                        if (offset < n_tx) {
                            offset += 100
                            prevAddress = walletInput.text.toString()
                            launch(Dispatchers.Main) {
                                progressBarTr.visibility = View.VISIBLE
                                val result = withContext(Dispatchers.IO){
                                    myViewModel.getDataAddress(prevAddress, offset)
                                }
                                if (result.isSuccessful){
                                    result.body()?.let {
                                        for (transaction in it.txs){
                                            transactionsAdapter.addTransaction(transaction)
                                        }
                                    }
                                }
                                progressBarTr.visibility = View.GONE
                            }
                        }
                        Log.d("scroll", "offset: $offset, n_tx: $n_tx")
                    }
                }

                // отслеживание нажатия галочки на клавиатуре
                walletInput.setOnEditorActionListener { _, i, _ ->

                    if (i == EditorInfo.IME_ACTION_DONE){
                        setDataAddress()
                    }
                    return@setOnEditorActionListener false
                }

                // слушатель долгого нажатия для копирования текста
                val onCopy = onCopyClickListener(activity, clipboard)

                // установка слушателя на TextView
                balance.setOnLongClickListener(onCopy)
                totalReceived.setOnLongClickListener(onCopy)
                totalSent.setOnLongClickListener(onCopy)
                totalTransactions.setOnLongClickListener(onCopy)
            }

        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            myViewModel.address.observe(viewLifecycleOwner) {
                //всего получено биткоинов
                totalReceived.text = getString(
                    R.string.price_transaction,
                    it.f_total_received
                )

                //всего отправлено биткоинов
                totalSent.text = getString(
                    R.string.price_transaction,
                    it.f_total_sent
                )

                //текущий баланс
                balance.text = getString(
                    R.string.price_transaction,
                    it.f_final_balance
                )

                //всего транзакций
                totalTransactions.text = it.n_tx.toString()
                n_tx = it.n_tx

                //очистка транзакций перед выводом новых
                transactionsAdapter.clear()

                //заполнение транзакций
                for (transaction in it.txs){
                    transactionsAdapter.addTransaction(transaction)
                }
                scroll.visibility = View.VISIBLE
            }
        }
    }

    private fun setDataAddress(){
        binding.apply {
            launch(Dispatchers.Main) {
                walletInputLayout.error = null
                progressBar.visibility = View.VISIBLE
                val result = withContext(Dispatchers.IO){
                    myViewModel.getDataAddress(walletInput.text.toString(), offset)
                }
                progressBar.visibility = View.GONE
                if (!result.isSuccessful){
                    if (result.code() == 429){
                        walletInputLayout.error = getString(R.string.too_many_requests)
                    } else {
                        walletInputLayout.error = getString(R.string.address_error)
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String?) =
            AddressFragment().apply {
                arguments = Bundle().apply {
                    putString(addressParam, param1)
                }
            }
    }
}