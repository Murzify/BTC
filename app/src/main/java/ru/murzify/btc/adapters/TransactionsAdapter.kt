package ru.murzify.btc.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.murzify.btc.R
import ru.murzify.btc.data.api.models.Tx
import ru.murzify.btc.databinding.TransactionItemBinding
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TransactionsAdapter(private val onItemClicked: (hash: String) -> Unit): RecyclerView.Adapter<TransactionsAdapter.TransactionsHolder>() {
    private val transactions = ArrayList<Tx>()

    class TransactionsHolder(
        item: View,
        context: Context,
        private val onItemClicked: (hash: String) -> Unit
    ): RecyclerView.ViewHolder(item){

        private val binding = TransactionItemBinding.bind(item)
        private val con = context


        fun bind(transaction: Tx){
            binding.apply {

                if (transaction.result > 0){
                    cost.text = "+" + con.getString(
                        R.string.price_transaction,
                        transaction.result.toDouble() / 100000000
                    )
                    cost.setTextColor(Color.GREEN)
                }else{
                    cost.text = con.getString(
                        R.string.price_transaction,
                        transaction.result.toDouble() / 100000000
                    )
                    cost.setTextColor(Color.RED)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    date.text = Timestamp(transaction.time * 1000)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .format(
                            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                        )
                }
                card.setOnClickListener{
                    onItemClicked(transaction.hash)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return TransactionsHolder(view, parent.context, onItemClicked)
    }

    override fun onBindViewHolder(holder: TransactionsHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun addTransaction(transaction: Tx){
        Log.d("add", "add")
        transactions.add(transaction)
        notifyItemChanged(itemCount + 1)
    }
    fun clear(){
        transactions.clear()
        notifyDataSetChanged()
    }
}