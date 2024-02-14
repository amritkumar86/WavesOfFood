package com.example.wavesoffood.adaptar

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wavesoffood.databinding.RecentBuyItemBinding

class RecentBuyAdapter(private var context : Context, private var foodnameslist:ArrayList<String>, private var foodimagelist:ArrayList<String>, private var foodpricelist : ArrayList<String>,private var foodquanitylist : ArrayList<Int>): RecyclerView.Adapter<RecentBuyAdapter.RecentBuyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentBuyViewHolder {
        val binding = RecentBuyItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecentBuyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentBuyViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int  = foodnameslist.size

    inner class RecentBuyViewHolder(private val binding: RecentBuyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                foodname.text = foodnameslist[position]
                foodprice.text = foodpricelist[position]
                foodquantity.text = foodquanitylist[position].toString()
                val uriString = foodimagelist[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(foodimage)
            }
        }

    }
}