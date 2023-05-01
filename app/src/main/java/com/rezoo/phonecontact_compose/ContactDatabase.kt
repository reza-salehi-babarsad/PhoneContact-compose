package com.rezoo.phonecontact_compose

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Contact::class],
    version = 1
)
abstract class ContactDatabase:RoomDatabase() {
    abstract val  dao:ContactDao
}