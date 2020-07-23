package com.example.geomob.DB

import androidx.room.*
import com.example.geomob.Classes.*

@Dao
interface PaysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPays(pays: Pays)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRessource(ressource: Ressource)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEvenement(evenement: Evenement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPaysPhoto(paysPhoto: PaysPhoto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPaysVideo(paysVideo: PaysVideo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPersonalite(personalite: Personalite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPersonalitePhoto(personalitePhoto: PersonalitePhoto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTweet(tweet: Tweet)

    @Update
    fun updatePays(pays: Pays)

    @Update
    fun updateRessource(ressource: Ressource)

    @Update
    fun updateEvenement(evenement: Evenement)

    @Update
    fun updatePaysPhoto(paysPhoto: PaysPhoto)

    @Update
    fun updatePaysVideo(paysVideo: PaysVideo)

    @Update
    fun updatePersonalite(personalite: Personalite)

    @Update
    fun updatePersonalitePhoto(personalitePhoto: PersonalitePhoto)

    @Update
    fun updateTweet(tweet: Tweet)

    @Delete
    fun deletePays(pays: Pays)

    @Delete
    fun deleteRessource(ressource: Ressource)

    @Delete
    fun deleteEvenement(evenement: Evenement)

    @Delete
    fun deletePaysPhoto(paysPhoto: PaysPhoto)

    @Delete
    fun deletePaysVideo(paysVideo: PaysVideo)

    @Delete
    fun deletePersonalite(personalite: Personalite)

    @Delete
    fun deletePersonalitePhoto(personalitePhoto: PersonalitePhoto)

    @Delete
    fun deleteTweet(tweet: Tweet)

    @Query("Select * from Pays Order By seen DESC, nomPays")
    fun getAllPays(): List<Pays>

    @Query("Select * from Pays WHERE seen = 0")
    fun getUnseenPays(): List<Pays>

    @Query("Select * from Pays Where codePays = :countryCode")
    fun findPaysByCountryCode(countryCode : String): List<Pays>

    @Query("Select * from Ressource")
    fun getAllRessources(): List<Ressource>

    @Query("Select * from Ressource where codePays = :countryCode")
    fun getAllRessourcesByCountryCode(countryCode : String): List<Ressource>

    @Query("Select * from Personlaite where codePays = :countryCode")
    fun getAllPersonalitesByPaysCountryCode(countryCode : String): List<Personalite>

    @Query("Select * from Tweet where codePays = :countryCode")
    fun getAllTweetsByPaysCountryCode(countryCode : String): List<Tweet>

    @Query("Select * from PaysPhoto where codePays = :countryCode")
    fun getAllPaysPhotoByPaysCountryCode(countryCode : String): List<PaysPhoto>

    @Query("Select * from PaysVideo where codePays = :countryCode")
    fun getAllPaysVideoByPaysCountryCode(countryCode : String): List<PaysVideo>

    @Query("Select * from Evenement where codePays = :countryCode")
    fun getAllEvenementByPaysCountryCode(countryCode : String): List<Evenement>

    @Query("Select * from PersonalitePhoto where idPersonalite = :personaliteId")
    fun getAllPersonalitesPhotoByPaysId(personaliteId : Long): List<PersonalitePhoto>

    @Query("Update Pays SET seen = 1 WHERE codePays = :countryCode")
    fun markAsSeen(countryCode: String)
}