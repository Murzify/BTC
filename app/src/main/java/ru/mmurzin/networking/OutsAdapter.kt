package ru.mmurzin.networking


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mmurzin.networking.api.blockchainInfo.responce.Out
import ru.mmurzin.networking.databinding.InputOutItemBinding

class OutsAdapter: RecyclerView.Adapter<OutsAdapter.InputsHolder>() {
    val outs = ArrayList<Out>()

    class InputsHolder(item: View, context: Context): RecyclerView.ViewHolder(item){
        val binding = InputOutItemBinding.bind(item)
        val con = context
        fun bind(out: Out){
            val clipboard = con.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            binding.apply {
                witnessRow.visibility = View.GONE
                script.text = out.script
                address.text = out.addr
                val value = out.value.toDouble() / 100000000
                price.text = con.getString(
                    R.string.price_transaction,
                    value)

                // копирование данных входа
                copyAddress.setOnClickListener {
                    copyData(clipboard, "address", address.text.toString())
                }
                copyScript.setOnClickListener{
                    copyData(clipboard, "address", script.text.toString())
                }
                copyWitness.setOnClickListener {
                    copyData(clipboard, "address", witness.text.toString())
                }


            }
        }
        fun copyData(clipboard: ClipboardManager, label: String, text: String){
            val clip: ClipData = ClipData.newPlainText(label, text)
            clipboard.setPrimaryClip(clip)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InputsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.input_out_item, parent, false)
        return InputsHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: InputsHolder, position: Int) {
        holder.bind(outs[position])
    }

    override fun getItemCount(): Int {
        return outs.size
    }

    fun addOut(out: Out){
        outs.add(out)
        notifyDataSetChanged()
    }
    fun clear(){
        outs.clear()
    }

}