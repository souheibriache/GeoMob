package com.example.geomob.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geomob.Activities.PaysActivity
import com.example.geomob.Classes.Ressource
import com.example.geomob.R

class ResourceAdapter (val activity : PaysActivity, val list : ArrayList<Ressource>) : RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>() {
    class ResourceViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val titleRes: TextView = v.findViewById<TextView>(R.id.resourceTitle)
        val descRes: TextView = v.findViewById<TextView>(R.id.resourceDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        return ResourceViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.resource_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val res = list[position]

        holder.titleRes.text = res.nomRessource
        holder.descRes.text = res.description

    }
}