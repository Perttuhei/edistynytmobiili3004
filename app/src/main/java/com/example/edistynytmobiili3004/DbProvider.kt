package com.example.edistynytmobiili3004

import android.content.Context
import androidx.room.Room

object DbProvider {

    lateinit var  db: AccountDatabase

    fun provide(context: Context) {
        db = Room.databaseBuilder(context, AccountDatabase::class.java, "account.db").build()
    }
}