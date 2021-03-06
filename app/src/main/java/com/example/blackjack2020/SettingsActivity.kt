package com.example.blackjack2020

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blackjack2020.MainActivity.Companion.SET_KEY
import com.example.blackjack2020.database.DatabaseHandler
import com.example.blackjack2020.models.SettingModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val adapter = ProfileAdapter(this,getItemsList())
        set_ItemsList.adapter = adapter
        set_ItemsList.layoutManager = LinearLayoutManager(this)

        val options = intent.getStringExtra(SET_KEY)
        if (options != null) {
            val toSet = Gson().fromJson<SettingModel>(options, SettingModel::class.java)
            difficulty = toSet.difficulty
            when (difficulty) {
                "set_ai_easy_btn" -> set_ai_easy_btn.isChecked = true
                "set_ai_normal_btn" -> set_ai_normal_btn.isChecked = true
                "set_ai_hard_btn" -> set_ai_hard_btn.isChecked = true
            }
            card = toSet.card
            when (card) {
                "cardface1" -> cardface1.isChecked = true
                "cardface2" -> cardface2.isChecked = true
                "cardface3" -> cardface3.isChecked = true
            }
            ProfileName = toSet.profileName
            set_profile_name.setText(ProfileName)
            set_curr_funds.text = TotalFunds.toString()

            set_insert_funds.setText("0")
        }

        set_return_btn.setOnClickListener(this)
        set_add_funds.setOnClickListener(this)
        set_clear_btn.setOnClickListener(this)
        btnAdd.setOnClickListener { view ->
            addRecord()
        }

        setupListofDataIntoRecyclerView()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.set_clear_btn -> {
                set_ai_easy_btn.isChecked = true
                cardface1.isChecked = true
                set_profile_name.setText("")
                set_curr_funds.text = "0.0"
                set_insert_funds.setText("0")

            }
            R.id.set_add_funds -> {
                var insertFunds = set_insert_funds.editableText.toString()
                val fundsAdd = insertFunds.toDouble()
                var currFund = set_curr_funds.text.toString()
                val currFunAdd = currFund.toDouble()
                val newFunds = addToFunds(currFunAdd, fundsAdd)

                set_curr_funds.text = newFunds.toString()
                set_insert_funds.setText("0")

            }
            R.id.set_return_btn -> {
                val intent = Intent()
                when (aiGroup.checkedRadioButtonId) {
                    R.id.set_ai_easy_btn -> {
                        difficulty = "set_ai_easy_btn"
                    }
                    R.id.set_ai_normal_btn -> {
                        difficulty = "set_ai_normal_btn"
                    }
                    R.id.set_ai_hard_btn -> {
                        difficulty = "set_ai_hard_btn"
                    }
                }
                when (cardGroup.checkedRadioButtonId) {
                    R.id.cardface1 -> {
                        card = "cardface1"
                    }
                    R.id.cardface2 -> {
                        card = "cardface2"
                    }
                    R.id.cardface3 -> {
                        card = "cardface3"
                    }
                }
                ProfileName = set_profile_name.editableText.toString()
                val cash = set_curr_funds.text.toString()
                try {
                    val totalCash = cash.toDouble()
                    TotalFunds = totalCash
                    setProfileName(ProfileName)
                    setDifficulty(difficulty)
                    setCard(card)
                    val setting = SettingModel(1, difficulty, card, ProfileName, TotalFunds)
                    val json = Gson().toJson(setting)
                    intent.putExtra(SETTING_EXTRA_KEY, json)
                    setResult(Activity.RESULT_OK, intent)
                    Log.i(TAG, intent.toString())
                    finish()
                } catch (ex: Exception) {
                    Toast.makeText(this, "Invalid somehow", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun addRecord() {
        var ai = ""
        var card = ""
        when (aiGroup.checkedRadioButtonId) {
            R.id.set_ai_easy_btn -> {
                ai = "set_ai_easy_btn"
            }
            R.id.set_ai_normal_btn -> {
                ai = "set_ai_normal_btn"
            }
            R.id.set_ai_hard_btn -> {
                ai = "set_ai_hard_btn"
            }
        }
        when (cardGroup.checkedRadioButtonId) {
            R.id.cardface1 -> {
                card = "cardface1"
            }
            R.id.cardface2 -> {
                card = "cardface2"
            }
            R.id.cardface3 -> {
                card = "cardface3"
            }
        }
        val name = set_profile_name.editableText.toString()
        val cash = set_curr_funds.text.toString()

        val databaseHandler = DatabaseHandler(this)
        val totalCash = cash.toDouble()
        TotalFunds = totalCash
        val status = databaseHandler.addUser(SettingModel(0, ai, card, name, TotalFunds))
        if (status > -1) {
            Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
            set_profile_name.text.clear()
            set_curr_funds.setText("0")
            set_insert_funds.setText("0")
        }
        set_ItemsList.adapter?.notifyDataSetChanged()
        setupListofDataIntoRecyclerView()
    }

    fun deleteRecord(settingModel: SettingModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setMessage("Are you sure you wants to delete ${settingModel.profileName}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //Code for the Delete part
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            val databaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteUser(SettingModel(settingModel.id, "", "", "", 0.0))
            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()
                setupListofDataIntoRecyclerView()
            }
            dialogInterface.dismiss() // Dialog will go poof
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will go poof
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()
        set_ItemsList.adapter?.notifyDataSetChanged()

    }

    fun updateRecord(settingModel: SettingModel) {

        var difficulty = ""
        when (aiGroup.checkedRadioButtonId) {
            R.id.set_ai_easy_btn -> {
                difficulty = "set_ai_easy_btn"
            }
            R.id.set_ai_normal_btn -> {
                difficulty = "set_ai_normal_btn"
            }
            R.id.set_ai_hard_btn -> {
                difficulty = "set_ai_hard_btn"
            }
        }
        var card = ""
        when (cardGroup.checkedRadioButtonId) {
            R.id.cardface1 -> {
                card = "cardface1"
            }
            R.id.cardface2 -> {
                card = "cardface2"
            }
            R.id.cardface3 -> {
                card = "cardface3"
            }
        }
        val name = set_profile_name.text.toString()
        val funds = set_curr_funds.text.toString()

        val databaseHandler = DatabaseHandler(this)

        if (!name.isEmpty()) {
            val cash = funds.toDouble()
            val status = databaseHandler.updateUser(
                SettingModel(
                    settingModel.id,
                    difficulty,
                    card,
                    name,
                    cash
                )
            )
            if (status > -1) {
                Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG).show()
                setupListofDataIntoRecyclerView()
                set_ItemsList.adapter?.notifyDataSetChanged()

            }
        } else {
            Toast.makeText(applicationContext, "Name cannot be blank", Toast.LENGTH_LONG).show()
        }
    }



    fun getUser(settingModel: SettingModel): ArrayList<SettingModel> {
        val databaseHandler = DatabaseHandler(this)
        val list: ArrayList<SettingModel> = databaseHandler.getUser(
            SettingModel(
                settingModel.id,
                settingModel.difficulty,
                settingModel.card,
                settingModel.profileName,
                settingModel.funds
            )
        )
        when (settingModel.difficulty) {
            "set_ai_easy_btn" -> set_ai_easy_btn.isChecked = true
            "set_ai_normal_btn" -> set_ai_normal_btn.isChecked = true
            "set_ai_hard_btn" -> set_ai_hard_btn.isChecked = true
        }

        when (settingModel.card) {
            "cardface1" -> cardface1.isChecked = true
            "cardface2" -> cardface2.isChecked = true
            "cardface3" -> cardface3.isChecked = true
        }
        set_profile_name.setText(settingModel.profileName)
        set_curr_funds.text = settingModel.funds.toString()

        set_insert_funds.setText("0")

        return list

    }


    private fun getItemsList(): ArrayList<SettingModel> {
        val databaseHandler = DatabaseHandler(this)
        val setList: ArrayList<SettingModel> = databaseHandler.viewAll()
        return setList
    }


    fun setupListofDataIntoRecyclerView() {
        if (getItemsList().size > 0) {
            set_ItemsList.visibility = View.VISIBLE
            set_NoRecordsAvailable.visibility = View.GONE
            set_ItemsList.layoutManager = LinearLayoutManager(this)
            val profileAdapter = ProfileAdapter(this, getItemsList())
            set_ItemsList.adapter = profileAdapter
        } else {
            set_ItemsList.visibility = View.GONE
            set_NoRecordsAvailable.visibility = View.VISIBLE
        }

        return
    }

    fun setDifficulty(Diff: String){
        difficulty= Diff
    }
    fun setCard(cardChoice: String){
        card= cardChoice
    }
    fun setProfileName(name: String){
        ProfileName= name
    }
    fun setTotalFunds(funds: Double){
        TotalFunds = funds
    }
    fun addToFunds(current: Double, adding: Double):Double{
        val newFunds = adding + current
        return newFunds
    }


    companion object {
        val SETTING_EXTRA_KEY = "SETTINGS"
        val TAG = "TESTING NOW"
        var ProfileName = ""
        var difficulty = "set_ai_easy_btn"
        var card = "cardface1"
        var TotalFunds = 0.0
    }
}