package com.example.geomob.Activities


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Database
import com.blongho.country_data.World

import com.example.geomob.Adapters.CountriesAdapter
import com.example.geomob.Classes.Initializer
import com.example.geomob.Classes.Pays
import com.example.geomob.DB.PaysDatabase
import com.example.geomob.R
import com.example.geomob.AppExecutors
import com.example.geomob.DB.PaysDao
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var paysDatabase : PaysDatabase
    lateinit var adapter: CountriesAdapter
    lateinit var layoutManager : LinearLayoutManager
    var countriesList = mutableListOf<Pays>()
    var mapStyleLoaded = false

    private lateinit var mapboxMap: MapboxMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        setContentView(R.layout.activity_main)

        World.init(this)

        Initializer.initializeDatabase(this)
        val pref = getSharedPreferences("PREF",0)
        val isInitialized = pref.getBoolean("init", false)

        //isInitialized = false

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = CountriesAdapter(this, countriesList)
        recyclerView.adapter = adapter


        if (!isInitialized){
            Initializer.initializeDatabase(this)
            val editor = pref.edit()
            editor.putBoolean("init", true)
            editor.apply()
        }

        paysDatabase =
            PaysDatabase.getDatabase(this)

        getPays()

        getUnseenPays()







    }

    fun selectCountry(countryCode : String){
        if (mapStyleLoaded && mapboxMap.style != null){
            mapboxMap.style!!.removeLayer("countriesLayer")

            val depthPolygonFillLayer = FillLayer("countriesLayer", "countriesSrc")

            depthPolygonFillLayer.withProperties(fillColor(rgb(102,178,255)))

            mapboxMap.style!!.addLayerAt(depthPolygonFillLayer, mapboxMap.style!!.layers.size-2)

            depthPolygonFillLayer.setFilter(
                eq(
                    id(),
                    literal(countryCode)
                )
            )

        }

    }

    private fun getPays(){
        AppExecutors.instance!!.diskIO().execute {
            countriesList.clear()
            val resultList = paysDatabase.paysDao().getAllPays()
            countriesList.addAll(resultList)
            Log.i("seeeeeeen", countriesList.toString())
            AppExecutors.instance!!.mainThread().execute{
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun getUnseenPays(){
        AppExecutors.instance!!.diskIO().execute {

            val resultList = paysDatabase.paysDao().getUnseenPays()
            if (resultList.isNotEmpty()){
                val pays = resultList[(resultList.indices).random()]
                AppExecutors.instance!!.mainThread().execute{

                    val d = Dialog(this)
                    d.setContentView(R.layout.dialog_discover)

                    d.findViewById<ImageView>(R.id.discoverCountryFlag)?.setImageResource(World.getFlagOf(pays.codePays))
                    d.findViewById<TextView>(R.id.discoverCountryTitle)?.text = pays.nomPays

                    d.findViewById<Button>(R.id.visitBtnView)?.setOnClickListener {
                        markAsSeenCountry(pays)
                        d.cancel()
                        val intent = Intent(this, PaysActivity::class.java)
                        intent.putExtra("countryCode", pays.codePays)
                        intent.putExtra("countryName", pays.nomPays)
                        startActivity(intent)
                    }

                    d.findViewById<ImageView>(R.id.closeDiscoverLayout)?.setOnClickListener {
                        d.cancel()

                    }

                    d.show()
                }
            }
        }
    }


    private fun markAsSeenCountry(country : Pays){
        AppExecutors.instance!!.diskIO().execute {
            val paysDatabase = PaysDatabase.getDatabase(this)
            paysDatabase.paysDao().markAsSeen(country.codePays)
            val index = countriesList.indexOf(country)
            countriesList[index].seen = true
            AppExecutors.instance!!.mainThread().execute {
                adapter.notifyDataSetChanged()
            }
        }
    }



}
