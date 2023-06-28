package com.example.finalmad

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.finalmad.Models.CryptoCurrency
import com.example.finalmad.databinding.FragmentDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DetailsFragment : Fragment() {

    lateinit var binding: FragmentDetailsBinding
    private val item: DetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        val data: CryptoCurrency = item.data!!

        setUpDetails(data)
        addToFav(data)
        binding.back.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.action_detailsFragment_to_HomeFragment)
        }
        // Inflate the layout for this fragment
        return (binding.root)
    }



    var favList: ArrayList<String>? = null
    var favListIsChecked = false

    private fun addToFav(data: CryptoCurrency) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = Firebase.database
        val myRef = database.getReference("Users/$userId")

        readData()
        favListIsChecked = if(favList!!.contains(data.symbol)){
            binding.favListButton.setImageResource(R.drawable.fav)
            true
        } else {
            binding.favListButton.setImageResource(R.drawable.fav_unselected)
            false
        }

        binding.favListButton.setOnClickListener{
            favListIsChecked =
                if (!favListIsChecked){
                    if(!favList!!.contains(data.symbol)){
                        favList!!.add(data.symbol)
                    }
                    storeData()
                    binding.favListButton.setImageResource(R.drawable.fav)
                    true
                } else {
                    binding.favListButton.setImageResource(R.drawable.fav_unselected)
                    favList!!.remove(data.symbol)
                    storeData()
                    false
                }
        }
    }

    private fun storeData(){
        val sharedPreferences = requireContext().getSharedPreferences("favList", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(favList)
        editor.putString("favList", json)
        editor.apply()
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("favList", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("favList", ArrayList<String>().toString())
        val type = object: TypeToken<ArrayList<String>>(){}.type
        favList = gson.fromJson(json, type)

    }

    @SuppressLint("SetTextI18n")
    private fun setUpDetails(data: CryptoCurrency) {

        binding.itemnameI.text = data.name
        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/128x128/"+ data.id +".png"
        ).into(binding.imageView4)
        binding.itemsymbolI.text = "(" +data.symbol + ")"
        binding.itemlabel.text = data.name + " Price"
        binding.itempriceI.text = "$"+String.format("%.02f", data.quotes[0].price)
        if(data.quotes[0].percentChange24h > 0){
            binding.item24h.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.item24h.text = "+${String.format("%.02f", data.quotes[0].percentChange24h)}%"
        }
        else if(data.quotes[0].percentChange24h == 0.0){
            binding.item24h.setTextColor(requireContext().resources.getColor(R.color.grey))
            binding.item24h.text = "+${String.format("%.02f", data.quotes[0].percentChange24h)}%"
        }
        else{
            binding.item24h.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.item24h.text = String.format("%.02f", data.quotes[0].percentChange24h)+"%"
        }

        if (data.quotes[0].marketCap >= 1000000000 || data.quotes[0].volume24h>= 1000000000) {
            binding.itemmcapI.text = "$"+String.format("%.2fB", data.quotes[0].marketCap/1000000000.0)
            binding.itemvolI.text= "$"+String.format("%.2fB", data.quotes[0].volume24h/1000000000.0)
        }

        else if( data.quotes[0].marketCap >= 1000000 || data.quotes[0].volume24h>= 1000000 ){
            binding.itemmcapI.text = "$"+String.format("%.2fM", data.quotes[0].marketCap/1000000.0)
            binding.itemvolI.text= "$"+String.format("%.2fM", data.quotes[0].volume24h/1000000.0)
        }

        else if( data.quotes[0].marketCap >= 1000 || data.quotes[0].volume24h>= 1000){
            binding.itemmcapI.text = "$"+String.format("%.2fk", data.quotes[0].marketCap/1000.0)
            binding.itemvolI.text= "$"+String.format("%.2fk", data.quotes[0].volume24h/1000.0)
        }

        binding.itempopI.text = data.cmcRank.toString()
    }

}