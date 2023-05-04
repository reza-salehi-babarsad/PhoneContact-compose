package com.rezoo.phonecontact_compose

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Upsert
    suspend fun upsertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM contact ORDER BY firstName ASC")
     fun getContactsOrderedByName():Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY lastName ASC")
     fun getContactsOrderedByLastName():Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY phoneNumber ASC")
     fun getContactsOrderedByPhoneNumber():Flow<List<Contact>>
}