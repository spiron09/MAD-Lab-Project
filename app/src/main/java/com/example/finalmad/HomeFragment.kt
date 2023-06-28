package com.example.finalmad

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.finalmad.Adapter.MarketAdapter
import com.example.finalmad.Api.ApiInterface
import com.example.finalmad.Api.ApiUtilities
import com.example.finalmad.Models.CryptoCurrency
import com.example.finalmad.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class HomeFragment : Fragment() {

     private lateinit var binding : FragmentHomeBinding
     private lateinit var list:List<CryptoCurrency>
     private lateinit var adapter: MarketAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        list = listOf()
        adapter = MarketAdapter(requireContext(), list, "home")
        binding.rView.adapter=adapter
//        val navController = NavHostFragment.findNavController(this)

        getMarketData()
        searchCoin()
        return binding.root
    }

    lateinit var searchText : String

    private fun searchCoin() {
        binding.crypsearchBar.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText = p0.toString().lowercase(Locale.ROOT)

                updateRecyclerView()
            }

        })
    }

    private fun updateRecyclerView(){
        val data = ArrayList<CryptoCurrency>()

        for (item in list){
            val coinName = item.name.lowercase(Locale.getDefault())
            val coinSymbol = item.symbol.lowercase(Locale.getDefault())

            if(coinName.contains(searchText) || coinSymbol.contains(searchText)){
                data.add(item)
            }
        }
        adapter.updateData(data)
    }


    private fun getMarketData() {
        lifecycleScope.launch ( Dispatchers.IO ){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()
            if(res.body() != null){
                withContext(Dispatchers.Main){
                    list = res.body()!!.data.cryptoCurrencyList
//                    binding.rView.adapter = MarketAdapter(requireContext(),res.body()!!.data.cryptoCurrencyList)
                    adapter.updateData(list)
                }
            }
        }
    }

}