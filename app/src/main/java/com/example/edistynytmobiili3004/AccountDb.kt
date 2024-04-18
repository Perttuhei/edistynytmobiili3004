package com.example.edistynytmobiili3004

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase


@Entity("account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accessToken: String
)

@Dao
abstract class AccountDao {
    @Insert
    abstract suspend fun addToken(entity: AccountEntity)

    @Query("SELECT accessToken FROM account LIMIT 1;")
    abstract  suspend fun getToken() : String?

}

@Database(entities = [AccountEntity::class], version=1)
abstract class AccountDatabae : RoomDatabase() {
    abstract fun accountDao() : AccountDao
}