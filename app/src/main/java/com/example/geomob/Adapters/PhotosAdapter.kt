package com.example.geomob.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.geomob.Activities.PaysActivity
import com.example.geomob.Classes.PaysPhoto
import com.example.geomob.R
import com.squareup.picasso.Picasso


class PhotosAdapter(val activity : PaysActivity, val list : ArrayList<PaysPhoto>) : RecyclerView.Adapter<PhotosAdapter.PaysPhotoViewHolder>(){
    class PaysPhotoViewHolder(v : View) : RecyclerView.ViewHolder(v){
        val paysPhotoImage: ImageView = v.findViewById<ImageView>(R.id.paysPhotoView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaysPhotoViewHolder {
        return PaysPhotoViewHolder(LayoutInflater.from(activity).inflate(R.layout.pays_photo_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PaysPhotoViewHolder, position: Int) {
        val photo = list[position]
        Picasso.get().load(photo.urlPhoto).into(holder.paysPhotoImage)
    }
}