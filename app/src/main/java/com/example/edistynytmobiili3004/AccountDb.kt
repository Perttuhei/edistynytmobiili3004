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
    val accessToken: String,
    val userId: Int = 0,
    val username: String = "",
    val roleId: Int = 0
)

@Dao
abstract class AccountDao {
    @Insert
    abstract suspend fun addAccount(entity: AccountEntity)

    @Query("SELECT userId FROM account ORDER BY id DESC LIMIT 1;")
    abstract  suspend fun getUserId() : Int

    @Query("SELECT accessToken FROM account ORDER BY id DESC LIMIT 1;")
    abstract  suspend fun getToken() : String?

    @Query("DELETE FROM account")
    abstract suspend fun removeTokens()

    @Query("SELECT COUNT(*) FROM account")
    abstract suspend fun getAccount(): Int

}

@Database(entities = [AccountEntity::class], version=1)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accountDao() : AccountDao

}