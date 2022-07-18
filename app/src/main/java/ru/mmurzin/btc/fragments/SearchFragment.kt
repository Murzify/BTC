package ru.mmurzin.btc.fragments

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import ru.mmurzin.btc.R
import ru.mmurzin.btc.Utils.validSearch
import ru.mmurzin.btc.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        activity?.let { activity ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            binding.apply {
                pasteBtn.setOnClickListener {
                    clipboard.primaryClip?.let {
                        val item = it.getItemAt(0)
                        searchInput.setText(item.text)
                        setFragment(searchInput.text.toString())
                    }
                }

                // отслеживание нажатия галочки на клавиатуре
                searchInput.setOnEditorActionListener { _, i, _ ->
                    if (i == EditorInfo.IME_ACTION_DONE){
                        setFragment(searchInput.text.toString())
                    }
                    return@setOnEditorActionListener false
                }

                // очистка ошибки при нажатии на поле ввода
                searchInput.setOnClickListener{
                    searchInputLayout.error = null
                }
            }
        }
        return binding.root
    }

    private fun setFragment(search: String) {
        when (validSearch(search)){
            "address" -> activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, AddressFragment.newInstance(search))
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
            "transaction" -> activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, TransactionFragment.newInstance(search))
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
            "block" -> activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, BlockFragment.newInstance(search))
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
            "invalid" -> binding.searchInputLayout.error = getString(R.string.invalid_search)
        }
    }
}