package com.rezoo.phonecontact_compose

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Upsert
    suspend fun upsertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM contact ORDER BY firstName ASC")
    suspend fun getContactsOrderedByName():Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY lastName ASC")
    suspend fun getContactsOrderedByLastName():Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY phoneNumber ASC")
    suspend fun getContactsOrderedByPhoneNumber():Flow<List<Contact>>
}