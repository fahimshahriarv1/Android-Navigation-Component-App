package database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserProfileDao {

    @Query("SELECT allInfo FROM user")
    suspend fun getAll(): List<String>
//
//    @Query("SELECT allInfo FROM user WHERE username LIKE :username")
//    fun getByUname(username: String): List<User>
//
    @Query("SELECT allInfo FROM user WHERE username LIKE :uname ")
    suspend fun findByUname(uname: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User) : Long
}