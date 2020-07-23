package com.example.geomob.Classes


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "PaysPhoto",
    foreignKeys = [ForeignKey(
        entity = Pays::class,
        parentColumns = arrayOf("codePays"),
        childColumns = arrayOf("codePays"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class PaysPhoto (@PrimaryKey val idPaysPhoto: Int,
                        val urlPhoto: String,
                        val codePays : String) {}