package com.rezoo.phonecontact_compose

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface ContactDao {
    @Upsert
    suspend fun upsertContact(contact: Contact)
}