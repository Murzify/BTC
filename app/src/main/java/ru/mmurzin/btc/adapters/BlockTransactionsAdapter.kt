package ru.mmurzin.btc.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mmurzin.btc.R
import ru.mmurzin.btc.databinding.BlockTransactionsItemBinding

class BlockTransactionsAdapter(private val onItemClicked: (hash: String) -> Unit): RecyclerView.Adapter<BlockTransactionsAdapter.TransactionsHolder>() {
    private val transactions = ArrayList<String>()

    class TransactionsHolder(
        item: View,
        private val onItemClicked: (hash: String) -> Unit
    ): RecyclerView.ViewHolder(item){

        private val binding = BlockTransactionsItemBinding.bind(item)


        fun bind(transaction: String){
            binding.apply {
                hash.text = transaction
                card.setOnClickListener{
                    onItemClicked(transaction)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.block_transactions_item, parent, false)
        return TransactionsHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: TransactionsHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun addTransaction(transaction: String){
        Log.d("add", "add")
        transactions.add(transaction)
        notifyDataSetChanged()
//        notifyItemChanged(itemCount + 1)
    }
    fun clear(){
        transactions.clear()
        notifyDataSetChanged()
    }
}