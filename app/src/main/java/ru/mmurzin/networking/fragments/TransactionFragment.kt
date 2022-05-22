package ru.mmurzin.networking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mmurzin.networking.R

private const val ARG_HASH = "hash"


class TransactionFragment : Fragment() {

    private var hash: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hash = it.getString(ARG_HASH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
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