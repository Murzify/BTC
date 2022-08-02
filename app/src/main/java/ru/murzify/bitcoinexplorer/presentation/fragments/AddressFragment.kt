package ru.murzify.bitcoinexplorer.presentation.fragments

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.murzify.bitcoinexplorer.R
import ru.murzify.bitcoinexplorer.presentation.SearchViewModel
import ru.murzify.bitcoinexplorer.databinding.FragmentAddressBinding
import ru.murzify.bitcoinexplorer.presentation.Utils.onCopyClickListener
import ru.murzify.btc.adapters.TransactionsAdapter


private const val addressParam = "address"

class AddressFragment : Fragment() {
    private val myViewModel: SearchViewModel by viewModel()
    private lateinit var address: String
    private lateinit var binding: FragmentAddressBinding
    private val transactionsAdapter = TransactionsAdapter{ hash ->
        onTransactionClick(hash)
    }

    private var n_tx = 0
    private var offset = 0
    private var prevAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        address = arguments!!.getString(addressParam).toString()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)
        activity?.let { activity ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            binding.apply {

                setDataAddress()

                // установка адапетра транзакций
                rvTransactions.layoutManager = LinearLayoutManager(activity)
                rvTransactions.adapter = transactionsAdapter

                scroll.setOnScrollChangeListener{ v, _, scrollY, _, _ ->
                    if (scrollY == (scroll.getChildAt(0).measuredHeight - v.measuredHeight)) {
                        if (offset < n_tx) {
                            offset += 100
                            prevAddress = address

                            progressBarTr.visibility = View.VISIBLE

                            myViewModel.getDataAddress(prevAddress, offset)

                        }
                    }
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
            myViewModel.addressData.observe(viewLifecycleOwner) {

                StatsFragment()

                progressBarTr.visibility = View.GONE

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

            progressBar.visibility = View.VISIBLE

            myViewModel.getDataAddress(address, offset)

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
        fun newInstance(param1: String) =
            AddressFragment().apply {
                arguments = Bundle().apply {
                    putString(addressParam, param1)
                }
            }
    }
}