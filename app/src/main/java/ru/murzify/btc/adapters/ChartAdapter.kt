package ru.murzify.btc.adapters

import com.robinhood.spark.SparkAdapter
import ru.murzify.btc.api.blockchainInfo.responce.Value

class ChartAdapter: SparkAdapter(){
    private var data:List<Value> = listOf()

    fun setData(list: List<Value>){
        data = list
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(index: Int): Any {
        return data[index]
    }

    override fun getY(index: Int): Float {
        return data[index].y.toFloat()
    }

    override fun getX(index: Int): Float {
        return data[index].x.toFloat()
    }

}