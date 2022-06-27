package ru.mmurzin.btc.fragments

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
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
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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


            }

        }

        // отслеживание нажатия галочки на клавиатуре
        binding.walletInput.setOnEditorActionListener { _, i, _ ->

            if (i == EditorInfo.IME_ACTION_DONE){
                setDataAddress()
            }
            return@setOnEditorActionListener false
        }
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
                    myViewModel.getDataAddress(walletInput.text.toString())
                }
                progressBar.visibility = View.GONE
                if (!result.isSuccessful){
                    walletInputLayout.error = getString(R.string.address_error)
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