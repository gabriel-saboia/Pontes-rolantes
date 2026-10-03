package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BacklogEntity
import com.example.data.model.ChecklistItemEntity
import com.example.data.model.EquipmentEntity
import com.example.data.model.FieldNoteEntity
import com.example.data.model.HistoryEntity
import com.example.data.model.PreventiveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipmentDao {
    @Query("SELECT * FROM equipment_table ORDER BY tag ASC")
    fun getAllEquipments(): Flow<List<EquipmentEntity>>

    @Query("SELECT * FROM equipment_table WHERE tag = :tag LIMIT 1")
    fun getEquipmentByTag(tag: String): Flow<EquipmentEntity?>

    @Query("SELECT * FROM equipment_table WHERE tag = :tag LIMIT 1")
    suspend fun getEquipmentByTagSync(tag: String): EquipmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipments(equipments: List<EquipmentEntity>)

    @Update
    suspend fun updateEquipment(equipment: EquipmentEntity)

    @Query("SELECT COUNT(*) FROM equipment_table")
    suspend fun getEquipmentCount(): Int
}

@Dao
interface PreventiveDao {
    @Query("SELECT * FROM preventives_table ORDER BY plannedDate ASC")
    fun getAllPreventives(): Flow<List<PreventiveEntity>>

    @Query("SELECT * FROM preventives_table WHERE equipmentTag = :tag ORDER BY plannedDate ASC")
    fun getPreventivesForTag(tag: String): Flow<List<PreventiveEntity>>

    @Query("SELECT COUNT(*) FROM preventives_table WHERE isCompleted = 0")
    fun getPendingCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM preventives_table WHERE isCompleted = 1")
    fun getCompletedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM preventives_table")
    fun getTotalCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreventives(preventives: List<PreventiveEntity>)

    @Update
    suspend fun updatePreventive(preventive: PreventiveEntity)

    @Query("DELETE FROM preventives_table")
    suspend fun clearAll()
}

@Dao
interface BacklogDao {
    @Query("SELECT * FROM backlogs_table ORDER BY isCritical DESC, createdAt DESC")
    fun getAllBacklogs(): Flow<List<BacklogEntity>>

    @Query("SELECT * FROM backlogs_table WHERE equipmentTag = :tag ORDER BY isCritical DESC, createdAt DESC")
    fun getBacklogsForTag(tag: String): Flow<List<BacklogEntity>>

    @Query("SELECT COUNT(*) FROM backlogs_table WHERE isCritical = 1")
    fun getCriticalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM backlogs_table")
    fun getTotalCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBacklogs(backlogs: List<BacklogEntity>)

    @Update
    suspend fun updateBacklog(backlog: BacklogEntity)

    @Query("DELETE FROM backlogs_table")
    suspend fun clearAll()
}

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history_table ORDER BY closedDate DESC, plannedDate DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history_table WHERE equipmentTag = :tag ORDER BY closedDate DESC")
    fun getHistoryForTag(tag: String): Flow<List<HistoryEntity>>

    @Query("SELECT COUNT(*) FROM history_table")
    fun getTotalCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistories(histories: List<HistoryEntity>)

    @Query("DELETE FROM history_table")
    suspend fun clearAll()
}

@Dao
interface ChecklistDao {
    @Query("SELECT * FROM checklist_items_table WHERE equipmentTag = :tag ORDER BY stepNumber ASC")
    fun getChecklistForTag(tag: String): Flow<List<ChecklistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItems(items: List<ChecklistItemEntity>)

    @Update
    suspend fun updateChecklistItem(item: ChecklistItemEntity)

    @Query("UPDATE checklist_items_table SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("UPDATE checklist_items_table SET measuredTolerance = :value WHERE id = :id")
    suspend fun updateTolerance(id: Long, value: Double)

    @Query("SELECT COUNT(*) FROM checklist_items_table WHERE equipmentTag = :tag AND status != 'PENDENTE'")
    fun getAnsweredCountForTag(tag: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM checklist_items_table WHERE equipmentTag = :tag")
    fun getTotalStepsForTag(tag: String): Flow<Int>
}

@Dao
interface FieldNoteDao {
    @Query("SELECT * FROM field_notes_table WHERE equipmentTag = :tag ORDER BY id DESC")
    fun getNotesForTag(tag: String): Flow<List<FieldNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: FieldNoteEntity)
}
