package ru.mmurzin.btc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mmurzin.btc.R
import ru.mmurzin.btc.databinding.FragmentAddressBinding


private const val addressParam = "address"

class AddressFragment : Fragment() {

    private var address: String? = null
    private lateinit var binding: FragmentAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            address = it.getString(addressParam)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
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