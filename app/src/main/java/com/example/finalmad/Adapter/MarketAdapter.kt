package com.example.finalmad.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalmad.FavFragmentDirections
import com.example.finalmad.HomeFragmentDirections
//import com.example.finalmad.HomeFragmentDirections
//import com.example.finalmad.HomeFragmentDirections
//import com.example.finalmad.HomeFragmentDirections
//import com.example.finalmad.HomeFragmentDirections
import com.example.finalmad.Models.CryptoCurrency
import com.example.finalmad.R
import com.example.finalmad.databinding.ListItemBinding

class MarketAdapter(var context: Context, var list: List<CryptoCurrency>, var type: String):RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {
    inner class MarketViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var binding = ListItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return MarketViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(dataItem: List<CryptoCurrency>){
        list = dataItem
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val item = list[position]

        holder.binding.itemname.text = item.name

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/"+ item.id +".png"
        ).into(holder.binding.imageView)

        holder.binding.itemprice.text = "$"+String.format("%.02f", item.quotes[0].price)
        holder.binding.itemnum.text = item.cmcRank.toString()
        holder.binding.itemvalchange.text = "$"+String.format("%.02f", (((item.quotes[0].percentChange24h) / 100) * (item.quotes[0].price)))

        if(item.quotes[0].percentChange24h > 0){
            holder.binding.itemperchange.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.itemperchange.text = "+${String.format("%.02f", item.quotes[0].percentChange24h)}%"
        }
        else if(item.quotes[0].percentChange24h == 0.0){
            holder.binding.itemperchange.setTextColor(context.resources.getColor(R.color.grey))
            holder.binding.itemperchange.text = "+${String.format("%.02f", item.quotes[0].percentChange24h)}%"
        }
        else{
            holder.binding.itemperchange.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.itemperchange.text = String.format("%.02f", item.quotes[0].percentChange24h)+"%"
        }

        if(type == "home"){
            holder.itemView.setOnClickListener{
                findNavController(it).navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
                )
            }
        }

        else if(type == "fav"){
            holder.itemView.setOnClickListener{
                findNavController(it).navigate(
                    FavFragmentDirections.actionFavFragmentToDetailsFragment(item)
                )
            }
        }


    }
}