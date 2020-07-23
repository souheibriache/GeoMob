package com.example.geomob.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.World

import com.example.geomob.Activities.MainActivity
import com.example.geomob.Activities.PaysActivity
import com.example.geomob.Classes.Pays
import com.example.geomob.DB.PaysDatabase
import com.example.geomob.R
import com.example.geomob.AppExecutors
import de.hdodenhof.circleimageview.CircleImageView

class CountriesAdapter(val activity : MainActivity, val list : MutableList<Pays>) : RecyclerView.Adapter<CountriesAdapter.WordViewHolder>(){
    class WordViewHolder(v : View) : RecyclerView.ViewHolder(v){
        val countryTitleView: TextView = v.findViewById<TextView>(R.id.countryTitleView)
        val itemLayout: RelativeLayout = v.findViewById<RelativeLayout>(R.id.countryItemLayout)
        val countryFlag: CircleImageView = v.findViewById<CircleImageView>(R.id.countryFlagView)
        val countryCapitalView: TextView = v.findViewById<TextView>(R.id.countryCapitalView)
        val countryButtonView: ImageView = v.findViewById<ImageView>(R.id.arrowView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val pays = list[position]
        holder.countryTitleView.text = pays.nomPays
        holder.countryCapitalView.text = pays.capital
        holder.countryFlag.setImageResource(World.getFlagOf(pays.codePays))

        holder.countryButtonView.setOnClickListener {
            markAsSeenCountry(position)
            val intent = Intent(activity, PaysActivity::class.java)
            intent.putExtra("countryCode", pays.codePays)
            intent.putExtra("countryName", pays.nomPays)
            activity.startActivity(intent)
        }

        holder.itemLayout.setOnClickListener {
            activity.selectCountry(pays.codePays)
        }

        if (pays.seen){
            holder.countryTitleView.setTextColor(activity.resources.getColor(R.color.seenCountry))
        }
    }

    private fun markAsSeenCountry(position: Int){
        AppExecutors.instance!!.diskIO().execute {
            val paysDatabase = PaysDatabase.getDatabase(activity)
            paysDatabase.paysDao().markAsSeen(list[position].codePays)
            list[position].seen = true
            AppExecutors.instance!!.mainThread().execute {
                notifyDataSetChanged()
            }
        }
    }
}