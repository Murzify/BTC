package ru.mmurzin.networking


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mmurzin.networking.api.blockchainInfo.responce.Input
import ru.mmurzin.networking.databinding.InputOutItemBinding

class InputsAdapter: RecyclerView.Adapter<InputsAdapter.InputsHolder>() {
    val inputs = ArrayList<Input>()

    class InputsHolder(item: View, context: Context): RecyclerView.ViewHolder(item){
        val binding = InputOutItemBinding.bind(item)
        val con = context
        fun bind(input: Input){
            val clipboard = con.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            binding.apply {
                witness.text = input.witness
                script.text = input.prev_out.script
                address.text = input.prev_out.addr
                val value = input.prev_out.value.toDouble() / 100000000
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
        holder.bind(inputs[position])
    }

    override fun getItemCount(): Int {
        return inputs.size
    }

    fun addInput(input: Input){
        inputs.add(input)
        notifyDataSetChanged()
    }
    fun clear(){
        inputs.clear()
    }

}