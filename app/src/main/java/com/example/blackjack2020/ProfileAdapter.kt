package com.example.blackjack2020

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.blackjack2020.SettingsActivity.Companion.ProfileName
import com.example.blackjack2020.models.SettingModel
import kotlinx.android.synthetic.main.user_row.view.*

class ProfileAdapter(val context: Context, val items: ArrayList<SettingModel>) :
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

        if (holder.userName.text != ProfileName) {
            holder.Edit.visibility = View.INVISIBLE

        }
        holder.Edit.setOnClickListener { view ->
            if (context is SettingsActivity) {
                context.updateRecord(item)
            }
        }


        holder.Delete.setOnClickListener { view ->
            if (context is SettingsActivity) {
                context.deleteRecord(item)
            }
        }

        holder.Use.setOnClickListener { view ->
            if (context is SettingsActivity) {
                context.getUser(item)
                context.setupListofDataIntoRecyclerView()

            }
        }

    }


    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val llMain = view.llMain
        val userName = view.user_name
        val userFunds = view.user_funds
        val Edit = view.ivEdit
        val Delete = view.ivDelete
        val Use = view.ivUse
    }


}