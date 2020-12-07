package com.example.blackjack2020

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.tip_view.view.*



class TipsAdapter(val tipsFeed: TipsNTricksActivity.TipsFeed) : RecyclerView.Adapter<CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.tip_view, parent, false)
        return CustomViewHolder(view)
    }
    override fun getItemCount(): Int {
        return tipsFeed.newTips.count()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val tips = tipsFeed.newTips.get(position)
        holder?.view?.tipNumber?.text = tips.tipName.toString()
        holder?.view?.tipText?.text = tips.tipText

    }

}
class CustomViewHolder (val view: View) : RecyclerView.ViewHolder(view){
}