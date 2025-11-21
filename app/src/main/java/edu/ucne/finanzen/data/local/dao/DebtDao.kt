package edu.ucne.finanzen.data.local.dao

import androidx.room.*
import edu.ucne.finanzen.data.local.entity.DebtEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface DebtDao {
    @Query("SELECT * FROM debts")
    fun observeAll(): Flow<List<DebtEntity>>

    @Query("SELECT * FROM debts WHERE debtId = :id")
    suspend fun getById(id: Int): DebtEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(debt: DebtEntity)

    @Delete
    suspend fun delete(debt: DebtEntity)

    @Query("DELETE FROM debts WHERE debtId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM debts WHERE status IN ('ACTIVE', 'OVERDUE')")
    fun getActiveCountFlow(): Flow<Int>

    @Query("SELECT SUM(remainingAmount) FROM debts WHERE status IN ('ACTIVE', 'OVERDUE')")
    fun getTotalRemainingFlow(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM debts")
    suspend fun getCount(): Int
}