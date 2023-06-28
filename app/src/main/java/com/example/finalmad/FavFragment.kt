package com.example.finalmad

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.finalmad.Adapter.MarketAdapter
import com.example.finalmad.Api.ApiInterface
import com.example.finalmad.Api.ApiUtilities
import com.example.finalmad.Models.CryptoCurrency
import com.example.finalmad.databinding.FragmentFavBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log


class FavFragment : Fragment() {
    private lateinit var binding: FragmentFavBinding
    private lateinit var favList : ArrayList<String>
    private lateinit var favListItem: ArrayList<CryptoCurrency>
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavBinding.inflate(layoutInflater)
        val uid = FirebaseAuth.getInstance().uid
        readData()
        binding.favback.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.action_favFragment_to_HomeFragment)
        }
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java)
                .getMarketData()

            if(res.body()!=null){
                withContext(Dispatchers.Main){
                    favListItem = ArrayList()
                    favListItem.clear()

                    for(favData in favList){
                        for (item in res.body()!!.data.cryptoCurrencyList){
                            if(favData == item.symbol){
                                favListItem.add(item)
                            }
                        }
                    }
                    //spinkit
//                    Log.e( "FavList",favListItem)
                    binding.favrView.adapter = MarketAdapter(requireContext(), favListItem, "fav")

                }
            }
        }
        return binding.root
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("favList", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("favList", ArrayList<String>().toString())
        val type = object: TypeToken<ArrayList<String>>(){}.type
        favList = gson.fromJson(json, type)

    }
}