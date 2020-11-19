package com.example.blackjack2020

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.blackjack2020.models.SettingModel
import kotlinx.android.synthetic.main.user_row.view.*

class ProfileAdapter(val context: Context, val items: ArrayList<SettingModel>):
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.user_row,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val item = items.get(position)

            holder.userName.text = item.profileName
            holder.userFunds.text = item.funds.toString()

            // Updating the background color according to the odd/even positions in list.
//            if (position % 2 == 0) {
//                holder.llMain.setBackgroundColor(
//                    ContextCompat.getColor(
//                        context,
//                        R.color.colorLightGray
//                    )
//                )
//            } else {
//                holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
//            }
        }

        /**
         * Gets the number of items in the list
         */
        override fun getItemCount(): Int {
            return items.size
        }

        /**
         * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
         */
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            // Holds the TextView that will add each item to
            val llMain = view.llMain
            val userName = view.user_name
            val userFunds = view.user_funds
            val ivEdit = view.ivEdit
            val ivDelete = view.ivDelete
        }


}